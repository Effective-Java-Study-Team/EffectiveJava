# 31장 - 한정적 와일드카드를 사용해 API 유연성을 높이라

태그: In progress

# 한정적 와일드 카드를 사용해야 하는 이유 (feat. 불공변성)

제네릭 타입은 불공변이라서 

List<Object> ≠ List<Integer> 이다.

이 불공변이라는 특성때문에 유연하지 못한 경우가 생기는 데 다음과 같다.

```java
public class Stack<E> {
		// ...

		// src 에 담긴 원소들을 stack 에 넣기
		public void pushAll(Iterable<E> src) {
        for(E e : src)
            push(e);
    }

		// 모든 원소를 c 로 옮기기
    public void popAll(Collection<E> dest) {
        while(isEmpty())
            c.add(pop());
    }
}
```

위와 같이 Stack 이 있다고 해보자.

문제가 없어 보이는 메서드들 이지만, 다음을 보자.

```java
GenericStackPushPopALL<Number> stack = new GenericStackPushPopALL<>();

Iterable<Integer> integers = new ArrayList<>();
stack.pushAll(integers); // 에러!

Collection<Object> c = new ArrayList<>();
stack.popAll(c); // 에러!
```

상식적인 관점에서는 Number 타입인 Stack 에다가/으로부터

1. Integer 를 넣는다
2. Object 를 가져와 Collection<Object> 에 넣는다.

인데 이 경우 문제가 생기기 마련이다.

왜냐하면 파라미터와 인자의 타입만 비교해보면 다음과 같은 데,

1. `Iterable<Number> vs Iterable<Integer>`
2. `Collection<Number> vs Collection<Object>`

불공변에 대한 설명을 보고 다시 이걸 보게 되면 

`당연히 다르네~` 라고 생각이 들거다.

이러한 불공변이라는 특성 때문에 타입 매개변수만 사용한다면

제네릭 타입은 유연하지 못하다.

# 한정적 와일드카드 타입 사용하는 법 1, 
매개변수와 PECS

- `PECS 란?`
    
    <aside>
    💡 `P` roducer - `E` xtends, `C` onsumer - `S`uper,
    즉, 매개변수 타입 T 가 
    생산자라면 <? extends T>, (생산 == 읽기 작업만 수행)
    소비자라면 <? super T>      (소비 == 쓰기 작업을 수행)
    
    </aside>
    
    위에서의 pushAll, popAll 메서드를 위 규칙대로 수정해보자.
    
    ```java
    // 생산자(producer)인 Iterable? -> extends!
    public void pushAll(Iterable<? extends E> src) { 
    		for(E e : src)
    				push(e);
    }
    
    // 소비자(consumer)인 Collection -> super!
    public void popAll(Collection<? super E> c) {
    		while(isEmpty())
    				c.add(pop());
    }
    ```
    
    ```java
    GoodGenericStackPushPopAll<Number> stack2 = new GoodGenericStackPushPopAll<>();
    stack2.pushAll(integers); // 잘 돌아간다!
    stack2.popAll(c); // 잘 돌아간다!
    ```
    
- `반환 타입에는 한정적 와일드카드 타입을 사용하면 오히려 불편함을 초래`
    
    두 집합을 하나의 집합으로 합치는 `union` 메서드의
    
    반환타입을 한정적 와일드카드 타입을 바꿔보자.
    
    ```java
    public static <E> Set<E> union(Set<E> s1, Set<E> s2) { 
    		Set<E> result = new HashSet<>(s1); 
    		result.addAll(s2);
    		return result;
    }
    
    ↓↓↓↓↓↓↓
    ↓↓↓↓↓↓↓
    ↓↓↓↓↓↓↓
    
    public static <E> Set<? extends E> union(Set<E> s1, Set<E> s2) { 
    		Set<E> result = new HashSet<>(s1); 
    		result.addAll(s2);
    		return result;
    }
    ```
    
    ```java
    Set<Integer> s1 = new HashSet<>();
    Set<Number> s2 = new HashSet<>();
    //...
    Set<? extends Number> union = union(s1, s2);
    ```
    
    사용자 입장에서 제네릭을 구현했는 지도 모르도록
    
    편하고 타입을 보장시켜주는 원래의 의도와는 다르게
    
    클라이언트가 한정적 와일드카드를 사용해야 되는
    
    불상사가 벌어지니, 절대로 반환타입에는 사용하지 말자.
    
    사용자가 API 의 와일드카드 타입에 신경을 써야된다면,
    
    해당 API 의 설계가 잘못되었다고 생각하고 고치자.
    
- `PECS 때문에 API가 복잡해지는 거 같아보여도 사용해야 된다.`
    
    ```java
    public static <E extends Comparable<E>> max(List<E> list);
    ```
    
    위와 같은 메서드를 PECS 규칙에 의거해서 바꿔보자.
    
    ```java
    public static <E extends Comparable<? super E>> 
    			E max(List<? extends E> list);
    ```
    
    1. list 는 생산자이기 때문에 extends 사용
    2. Comparable 은 소비자이기 때문에 super 사아ㅛㅇ

    보면서 느끼겠지만 정말 ~~더럽다~~ 복잡하다.
    
    하지만 위처럼 해야만 해결이 가능한 경우가 존재한다.
    
    바로 ScheduledFuture 이다.
    
    ```java
    List<ScheduledFuture<?>> scheduledFutures = ...;
    ```
    
    수정 전의 max 는 위 리스트를 처리할 수가 없는 데
    
    이는 ScheduledFuture 의 타입 계층도가 다음과 같기 때문에다.

  ![스크린샷 2023-08-28 오후 7.35.47.png](/Users/xpmxf4/Desktop/develop/EffectiveJava/EffectiveJavaStudy/Chapter5/CoRaveler/Item31/pictures/ScheduledFuture.png)
    
    ScheduledFuture<V> 는 Delayed 를 상속받기에
    
    Comparable<ScheduledFuture>를 구현하지 않고
    
    Comparable<Delayed> 를 상속받는다.
    
    즉, ScheduledFuture 객체끼리는 비교가 불가하다.
    
    따라서 이런 경우를 제외하기 위해서
    
    max 를 이처럼 바꿔줘야 한다.
    
    ~~(근데 이런 경우가 있나?)~~ 
    
- `Poly Expression 이란?`
    
    [Chapter 15. Expressions](https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html#jls-15.2)
    
    일반적으로 "poly expression"은 '다형성을 가진' 표현식을 의미한다.
    
    즉, 그 타입이 특정 상황에서 목표 타입에 맞게 적응할 수 있는 식을 의미하는 데,예를 들어, Java에서 람다 표현식은 목표로 하는 함수형 인터페이스의 예상 타입에 맞게 타입이 적응할 수 있으므로 poly expression으로 간주된다.
    
    > Lambda expressions are always poly expressions.
    
    JLS : [https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html#jls-15.27](https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html#jls-15.27)
    > 
    
    람다의 항상 poly expression 이지만 expression 의 종류에 따라 
    
    poly expression 인지 따지는 규칙이 존재한다.
    
    - 괄호로 묶인 표현식
    - 클래스 인스턴스 생성 표현식
    - 메서드 호출 표현식
    - 메서드 참조 표현식
    - 조건 표현식
    - 람다 표현식

# 한정적 와일드카드 타입 사용하는 법 2, 
“타입 매개변수 vs 와일드카드”

- swap 메서드
    
    ```java
    public static <E> void swap(List<E> list, int i, int j);
    public static void swap(List<?> list, int i, int j);
    ```
    
    swap 메서드는 이렇게 2 가지가 존재한다.
    
    저자는 메서드 선언에 따라 타입 매개변수가 한 번만 나오면
    
    와일드카드로 대체하라고 한다.
    
    ```java
    public static void swap(List<?> list, int i, int j) {
    		list.set(i, list.set(j, list.get(i)));
    }
    ```
    
    이 경우가 맞다고 하는 것이다.
    
    하지만 위 경우에서 list 의 현재 타입은 `capture of ?` 이기 때문에
    
    list.set 메서드는 타입 오류가 난다.
    
    따라서 별도의 swapHelper 메서드를 선언해야 한다.
    
    ```java
    public static void swap(List<?> list, int i, int j) {
    		swapHelper(list, i, j);
    }
    
    private static void swapHelper(List<E> list, int i, int j) {
    		list.set(i, list.set(j, list.get(i)));
    }
    ```
    
- 개인적으로 이런 경우는 타입 매개변수가 맞다고 생각함.
    
    ```java
    public static void swap(List<?> list, int i, int j) {
          // instead of using a raw type here, it's possible to capture
          // the wildcard but it will require a call to a supplementary
          // private method
          final List l = list;
          l.set(i, l.set(j, l.get(i)));
    }
    ```
    
    Collections.reverse 메서드는 이렇게 구현함
    
<br>
<br>

<aside>
💡 조금 복잡하더라도 와일드카드 타입을 적용하면 API 가 훨씬 유연해진다.

그러니 널리 쓰일 라이브러리를 작성한다면 반드시 와일드카드 타입을 적절히 사용해줘야 한다. 

PECS 공식을 기억하자.
즉, 생산자는 extends, 소비자는 super 를 사용한다.
Comparable과 Comparator는 모두 소비자라는 사실도 잊지 말자.

</aside>