# 32장 - 제네릭과 가변인수를 함께 쓸 때는 신중하라

태그: Done

# 제네릭 varargs 가 위험한 이유

제네릭 varargs 를 사용하게 되면 컴파일러는 다음과 같은 과정을 거치게 된다.

```java
static void method(List<E>... args){...}
static void method(List<Object>... args){...}
static void method(List<Object>[] args){...}
```

즉, 최종적으로는 제네릭 타입의 배열을 만들게 되는 데,

이는 자바의 문법상 금지되어 있기도 하고,

또한 힙오염을 일으킬 수 있어 위험하기도 하다.

- 힙오염 예시
    
    ```java
    static void heapPollution(List<String>... stringLists) {
    	List<Integer> list = List.of(42);
    	Object[] objs = stringLists;
    	objs[0] = list;
    	String el = objs[0].get(0); // ClassCastException!!
    }
    ```
    

하지만 아니러니하게도

```java
static void method(T... args);
```

는 가능하지만 (물론 경고가 뜨지만)

```java
new T[];
```

는 아얘 문법 오류가 나게 된다.

사실 같은 형태인데, 어째서 가변인자 배열에는 제네릭이 가능할까?

# 그럼에도 사용하는 이유

실무에서 유용하기 때문이다!

```java
static <T> List<T> asList(T... args) {
  List<T> result = new ArrayList<>();
	for(T e : args) 
		result.add(e);
	return result;
}

List list = asList("A", "B", "C");
String stringEl = list.get(0); // casting 안해도 된다!
```

```java
static Object List<Object> asList(Object[] args) {
  List<Object> result = new ArrayList<>();
	for(Object e : args) 
		result.add(e);
	return result;
}

List list = asList("A", "B", "C");
String stringEl = (String) list.get(0); // casting
```

위와 같은 차이가 존재하는 데, 

이게 메서드 한 개니까 별 거 없어보일 수 있지만

사실 이런 메서드가 수 백, 수천 개가 있다면 

매번 casting 하는 것이 정말 귀찮을 것이다.

그래서 자바 언어 설계자는 이 모순을 수용하기 하고,

대신 안전할 경우를 따져보고, 

안전할 경우 @SafeVarargs 애너테이션을 달도록 했다.

<aside>
💡 그 답은 제네릭이나 매개변수 화 타입의 varargs 매개변수를 받는 메서드가 실무에서 매우 유용하기 때문 이다. 그래서 언어 설계자는 이 모순을 수용하기로 했다.

이펙티브 자바, p.192

</aside>

# 메서드가 안전한 기준

1. 메서드가 varargs 배열에 아무것도 저장하지 않고,
2. 그 배열의 참조가 밖으로 노출되지 않으면
    
    ```java
    static <T> T[] toArray(T... args) {
    	return args
    }
    ```
    

안전하다 인데, 쉽게 얘기해서

> 순수하게 요소들을 전달하는 일만 한다면 (본래의 목적이기도 하다)
그 메서드는 안전하다.
> 
- 참조 노출시 에러나 가는 경우
    
    ```java
    static <T> T[] toArray(T... args) {
    	return args
    }
    ```
    
    2번의 참조를 밖으로 노출시키는 메서드가 있다고 해보자.
    
    ```java
    static <T> T[] pickTwo(T a, T b, T c) {
    	switch(ThreadLocalRandom.current().nextInt(3)) {
    		case 0: return toArray(a, b);
    		case 1: return toArray(b, c);
    		case 2: return toArray(c, a);
    	}
    	throw new AssertionError();
    }
    ```
    
    그리고 T 타입의 원소 a, b, c 중에서 2 개의 원소를
    
    랜덤하게 뽑는 메서드 pickTwo 메서드가 있다.
    
    여기까지 문법적으로 오류도 나지 않고,
    
    컴파일도 잘 된다.
    
    하지만 실제로 pickTwo 메서드를 호출해 결과값을 받아보면
    
    ```java
    public static void main(String[] args) {
    	String[] result = pickTwo("a", "b", "c"); // ClassCastException
    }
    ```
    
    왜냐하면 toArray 메서드는 컴파일 될 때 타입을 알 수가 없어
    
    Object 타입으로 바뀌기 때문이다.
    
- 외부에 노출해도 괜찮은 예외 경우
    1. @SafeVarargs 가 달린 메서드에 넘기는 경우
        
        ```java
        @SafeVarargs
        public static <T> void safeMethod(T... elements) {
            for (T element : elements) {
                System.out.println(element);
            }
        }
        
        public static void main(String[] args) {
            safeMethod("one", "two", "three");  // 안전
        }
        ```
        
    2. varargs 의 원소를 출력만 하는 경우 필요 없다.
        
        ```java
        public static <T> void genericMethod(T... elements) {
            normalMethod(elements);  // 안전하다고 가정
        }
        
        public static void normalMethod(Object[] array) {
            for (Object obj : array) {
                System.out.println(obj);
            }
        }
        
        public static void main(String[] args) {
            genericMethod("one", "two", "three");
        }
        ```
        
        <aside>
        💡 즉, 안전한 메서드에다 노출하는 것은 상관이 없다!
        
        </aside>
        

# 이 메서드는 안전하다, @SafeVarargs 규칙

<aside>
💡 제네릭이나 매개변수화 타입의 varargs 매개변수를 받는 모든 메서드에 @SafeVarargs 를 달자

</aside>

위에서 말한 안전한 기준을 만족한다면, 

@SafeVarargs 애너테이션을 달아서,

경고문을 없애자!