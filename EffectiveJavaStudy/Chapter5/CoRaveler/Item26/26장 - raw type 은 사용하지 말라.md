# 26장 - raw type 은 사용하지 말라

태그: In progress

# 1. 용어 정의

1. 제네릭 클래스/인터페이스란?
    - 클래스와 인터페이스 선언시,
        
        타입 매개변수를 사용하면 제네릭 클래스/인터페이스 라고 한다!
        
2. 제네릭 타입
    - 제네렉 클래스 + 제네릭 인터페이스 = 제네릭 타입
3. 매개변수화 타입(parameterized type) 이란?
    - 제네릭 타입 각각은 매개변수화 타입이라고도 한다.
        
        ex) List<String> 은 `String 원소를 가진 List` 라는 타입을 의미한다.
        
        `즉, List<String> 과 List<Object> 는 다른 타입이다!`
        
4. raw type 의 정의
    - 제네릭 타입에서 타입 매개변수를 전혀 사용하지 않을 때를 의미한다.
        
        ex) List<String> 의 경우 raw type 은 List 이다.
        

# 2. 제네릭 타입의 장점

1. `디버깅이 쉽고, 런타임이 아닌 컴파일 때 에러가 나도록 해준다.`
    
    > 오류는 가능한 한 발생 즉시,
    이상적으로는 컴파일 시에 잡는 것이 항상 좋다.
    
    이펙티브 자바, p155
    > 
    
    제네릭은 타입에 대한 명시를 통해,
    
    컴파일 타임 때 에러를 미리 잡을 수 있도록 한다.
    
    ```java
    // Generic Type vs Raw Type
    private final Collection BigDecimals = ...;
    
    BigDecimals.add(new BigInteger(...)); // 컴파일 타임때는 에러가 나지 않고,
    
    Iterator iter = BigDecimals.iterator();
    while(!iter.hasNext()) {
    	BigDecimal bd = (BigDecimal) iter.next(); // 런타임때 여기서 에러가 난다
    	bd.cancel();
    }
    ```
    
    위의 경우, 문제를 발생시킨 코드와 문제가 터지는 곳이 가까이 있어
    
    쉽게 고칠 수가 있다.
    
    하지만
    
    ```java
    private final Collection BigDecimals = ...;
    
    BigDecimals.add(new BigInteger(...));
    ```
    
    이 부분이 다른 곳에 선언이 되었고
    
    다른 곳에서 iterator 를 사용한다면? 
    
    정확히 문제가 터지는 곳을 찾지 못하고 개고생을 하게 될 것이다.
    
2. `원소를 꺼낼 시 실패하지 않음을 보장한다`
    
    이는 제네릭 타입을 구현하기 위해 자바가 `타입 소거` 를 사용했기 때문에
    
    실패하지 않음을 보장해준다.
    
    ```java
    public class Box<T> {
        private T value;
    
        public T getValue() {
            return value;
        }
    
        public void setValue(T value) {
            this.value = value;
        }
    }
    
    ...
    
    Box<Object> box = new Box<>();
    ...
    ```
    
    이런 코드는 실제로는 컴파일러가 다음과 같이 변환을 해준다.
    
    ```java
    public class Box {
        private Object value;
    
        public Object getValue() {
            return value;
        }
    
        public void setValue(Object value) {
            this.value = value;
        }
    }
    ```
    
    이를 보고 우리는 타입 소거라고 말하고,
    
    우리는 형변환을 보장받기 때문에 get, set 사용시
    
    안전함을 보장받는 다!
    

# 3. raw type 은 필요없는 거 같은 데 왜 있냐?

사실 애초에 안 쓰는 게 맞기는 하다만,

제네릭 타입은 JDK 9 이후에 출시가 되었고

정착되기 까지 10 년의 세월이 걸렸다.

따라서 그 전 코드들과의 호환성을 위해서

어쩔 수 없이 만들어야 했다.

```java
// JDK9 이전 코드
void nonGeneric(List list) {...}
```

위와 같은 레거시 코드를 만약 다음과 같은 코드를 사용한다 해보자.

```java
List<Integer> list = new List<>();
nonGeneric(list);
```

이 경우 제네릭을 사용하지 않는 레거시 메서드와

제네릭 타입을  인자로 넣었을 때의 레거시가 호환이 안된다면

수 많은 코드들이 빨간줄로 뒤덮일 것이다.

`즉, 레거시와 새로운 문법의 호환성을 위해 만들었다!`

이게 가능한 것은 아까도 얘기한 타입 소거때문이기도 하다.

```java
public class Box<T> {
    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}

Box box = new Box(); // ?
```

위의 경우, 제네릭 매개변수를 넣지 않았기 때문에

타입 소거시에 `Object` 타입으로 바뀌게 된다.

# 4. 비한정적 와일드카드 타입 `?` 이란?

근데 처음에도 얘기했듯 List<Object> 와 List<Integer> 는 엄연히 다른 타입이다.

```java
List<Object> list = new ArrayList<>();
list.add(2); // 는 가능하지만

void method(List<Object> list) {...}
에는 오로지 List<Object> 만 올 수 있다.
```

그렇다면 아무 매개변수 타입을 가진 제네릭 타입을 받는 건 대체 뭘 써야 할까?

raw type? → 라고 하면 두들겨 맞기 좋은 대답이다.

이를 위해서 비한정적 와일드카드, `?` 를 사용해야 한다.

### 정의

? 는 어떤 종류의 타입이든 허용되는 와일드카드를 의미한다.

자바에서 제네릭 타입의 매개변수를 지정할 때,

타입의 제한없이 모든 타입을 허용하고자 할 때 사용한다.

# 5. List vs List<Object> vs List<?>

그렇다면 타입의 위 3가지 경우를 비교해보자.

```java
static void processList(List list) {
        // ... processing
}

static void processListWildcard(List<?> list) {
				// ... processing
}

static void processListObject(List<Object> list) {
        // ... processing
}
```

3가지 메서드는 각기 raw type, 와일드카드 ?, 제네릭 타입인 list 를 매개변수로 받는 다.

```java
List<String> strList = new ArrayList<>();
List<Integer> intList = new ArrayList<>();
```

그리고 이렇게 2개의 string, integer 리스트를 선언한다 했을 때

2 개의 리스트들로 3 가지 메서드들을 호출해보자.

```java
processList(strList);
processList(intList);

processListWildcard(strList);
processListWildcard(intList);

        // 에러가 난다!
//        processListObject(strList);
//        processListObject(intList); 
```

첫 2개의 메서드는 호출이 오류없이 가능하다.

왜냐하면 2 개의 메서드 다 어느 타입의 List 를 다 받을 수 있으니깐

하지만 3번째 메서드의 파라미터 타입은 `List<Object>` 이다.

따라서 받을 수가 없다.

# 6. capture of ? 이란?

```java
Set<?> set = new HashSet<>();
set.add(1); // 에러
```

```java
java: incompatible types: int cannot be converted to capture#1 of ?
```

위와 같은 코드가 있을 때 set.add 에는 에러가 난다.

아무거나 다 받을 수 있다면서 왜 에러가 날까?

그리고 `capture#1 of ?` 는 또 뭘까?

[Understanding a captured type in Java (symbol '?')](https://stackoverflow.com/questions/30797805/understanding-a-captured-type-in-java-symbol#answer-30798066)

[Chapter 5. Conversions and Contexts](https://docs.oracle.com/javase/specs/jls/se8/html/jls-5.html#jls-5.1.10)

`와일드카드 타입의 캡처`는 컴파일러가 특정 위치에서 

와일드카드 타입의 특정 인스턴스의 타입을 나타내기 위해 사용하는 타입입니다.

```java
void method(Ex<?> e1, Ex<?> e2) {...}
```

예로, 두 와일드카드 매개변수를 가진 메서드 void m(Ex<?> e1, Ex<?> e2)가 있습니다. 

e1과 e2의 선언된 타입은 Ex<?>로 동일하게 작성되어 있지만, 

`런타임에서는 다르고 호환되지 않는 타입`을 가질 수 있습니다.

컴파일 도중, e1과 e2의 타입 매개변수는 

각 사용되는 타입 소거후, 위치마다 특정 타입이 부여됩니다. 

이때 이 새로운 타입을 `그들의 선언된 타입의 캡처`라고 불립니다.

이는 아얘 새로운 타입이기 때문에 Object 와도 호환이 안된다.

# 7. 와일드타입의 캡쳐

익명 클래스처럼 명명할 수는 없지만,

순간을 포착할 수 있는 데, 이를 캡쳐라고 한다.

```java
public void swapFirstTwo(List<?> list) {
    // 컴파일 오류!
    list.set(0, list.get(1)); 
    list.set(1, list.get(0));
}
```

둘다 `capture of ?` 타입의 원소가 아닐까 하면 

반 정도만 맞다고 하겠다.

![Untitled](/Users/xpmxf4/Desktop/develop/EffectiveJava/EffectiveJavaStudy/Chapter5/CoRaveler/Item26/pictures/Untitled.png)

실제로 이렇게 뜨니깐.

하지만 조금만 자세히 보자.

**`list.get(1)`**는 와일드카드(**`?`**)를 사용하는 리스트에서 객체를 가져온다. 

타입 소거로 인해 이 객체의 타입은 **`Object`**입니다.

그러나 **`list.set(0, list.get(1));`**에서 **`list.set(0, ...)`**는 

와일드카드(**`?`**) 타입의 객체만 허용된다.

여기서 `capture#1 of ?`는 컴파일러가 내부적으로 생성한 일종의 임시 이름인데, 

와일드카드 타입의 실제 타입을 "캡처"하는 과정에서 사용되며, 외부에서 직접 사용할 수 없는 이름이다.

즉, **`Object`** 타입의 객체 (즉, **`list.get(1)`**의 결과)를 

와일드카드(**`?`**) 타입의 위치에 설정하려고 했기 때문에 이 오류가 발생한거다.

<aside>
💡 실제로 이러한 특성 때문에 와일드카드 하나만 타입 매개변수로 넣는 경우,
즉  용도는 주로 객체의 불변성을 보장하기 위해서이다. 

좀 더 쉽게 풀자면, 아직 무슨 타입인지 모르는 애한테서는 get 같이 
불변성을 건드리지 않는 메서드만 호출이 가능하고
불변성을 건드리는 set 같은 경우에는 사용을 할 수 없도록 하는 것이다!

</aside>

그래서 이런 방법을 우회, 해결하기 위한 방법으로 `와일드카드 캡쳐 방법` 을 사용해야 한다.

```java
private static void swapFirstTwo(List<?> list) {
        swapHelper(list, 0, 1);
}

private static <T> void swapHelper(List<T> list, int i, int j) {    // captured!
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
}
```

# 8. Java Collections 의 reverse 는 어떻게 되어 있을까?

```java
@SuppressWarnings({"rawtypes", "unchecked"})
    public static void reverse(List<?> list) {
        int size = list.size();
        if (size < REVERSE_THRESHOLD || list instanceof RandomAccess) {
            for (int i=0, mid=size>>1, j=size-1; i<mid; i++, j--)
                swap(list, i, j);
        } else {
            // instead of using a raw type here, it's possible to capture
            // the wildcard but it will require a call to a supplementary
            // private method
            ListIterator fwd = list.listIterator();
            ListIterator rev = list.listIterator(size);
            for (int i=0, mid=list.size()>>1; i<mid; i++) {
                Object tmp = fwd.next();
                fwd.set(rev.previous());
                rev.set(tmp);
            }
        }
    }
```

```java
public static void swap(List<?> list, int i, int j) {
        // instead of using a raw type here, it's possible to capture
        // the wildcard but it will require a call to a supplementary
        // private method
        final List l = list;
        l.set(i, l.set(j, l.get(i)));
    }
```

실제로 방금 구현한 형태와 비슷하다!

else 블럭 안에 있는 방법도 사실 거의 비슷하지만

> 메서드의 구현을 살펴보면, 리스트의 크기가 크지 않거나 (**`size < REVERSE_THRESHOLD`**) 
리스트가 **`RandomAccess`** 인터페이스를 구현한 경우에는 직접 인덱스를 사용하여 원소를 바꿉니다. 
이 경우, **`swap`** 메서드를 사용하면 캡처된 와일드카드 방식을 통해 타입 안전성을 보장할 수 있습니다.
> 
> 
> 하지만, 큰 크기의 리스트(특히 **`LinkedList`**와 같은 non-random access 리스트)에서 
> 인덱스를 직접 사용하는 것은 효율적이지 않습니다. 
> 그래서 **`ListIterator`**를 사용하여 리스트의 앞쪽과 뒤쪽에서 동시에 원소를 가져와서 스왑합니다.
> 
> 여기서 문제는, **`List<?>`** 타입에서 **`ListIterator`** 객체를 얻을 때, 
> 해당 **`ListIterator`**는 **`?`** 와일드카드 타입의 원소를 반환하게 됩니다. 
> 이러한 원소를 직접 수정하려면 (예: **`fwd.set(...)`**, **`rev.set(...)`**) 
> 타입 안전성 문제가 발생할 수 있습니다.
> 
> 따라서, Java의 **`Collections`** 클래스 구현자들은 
> 이 문제를 해결하기 위해 두 가지 선택을 할 수 있습니다:
> 
> 1. 캡처된 와일드카드를 사용하는 추가의 private 메서드를 만들어 그 메서드에서 수정을 수행한다.
> 2. 타입 안전성 경고를 회피하기 위해 **`@SuppressWarnings`** 주석을 사용하고, raw 타입의 **`ListIterator`**를 사용하여 원소를 직접 수정한다.
> 
> 위의 **`reverse`** 메서드 구현에서는 후자의 방식을 선택하였습니다. 
> 주석에서도 언급하고 있듯이, 첫 번째 방법을 사용하여 캡처된 와일드카드를 
> 활용하는 추가적인 private 메서드를 만들 수도 있습니다. 
> 
> 그러나, 이 경우에는 성능상의 이점이나 코드의 간결성 등 여러 이유로 
> **`@SuppressWarnings`**와 raw 타입을 사용하는 방식을 선택하였을 가능성이 있습니다.
> 
> [출처] : gpt (feat 정리하기 두려웠다…)
> 

# 9. raw type 을 써야하는 예외 경우

1. class 리터럴에는 raw type 를 써야 한다.
    
    ```java
    List.class (O)
    
    List<String>.class (X) 
    List<?>.class (X)
    ```
    
2. instanceof 사용시 raw type 를 사용한다.
    
    ```java
    if (o instanceof Set) {
    	Set<?> s = (Set<?>) o;
    	...
    }
    ```
   

[Notion 링크](https://devchpark.notion.site/26-raw-type-834b30578888490cafa00ec9ee21c586?pvs=4)