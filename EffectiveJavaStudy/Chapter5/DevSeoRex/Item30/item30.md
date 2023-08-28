### 아이템 30 - 이왕이면 제네릭 메서드로 만들라

### 제네릭 메서드를 만들어야 하는 이유

```java
public static Set union(Set s1, Set s2) {
    Set result = new HashSet(s1);
    result.addAll(s2);
    return result;
}
```

이 `메서드`는 두 개의 `Set`을 받아서 `합집합`을 반환하는 유틸리티 `메서드`이다. <br>
이 메서드는 `컴파일`은 되지만 문제가 있는 `메서드`다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/cbf55ee8-d831-48a6-bb9d-77d096e4a9ec)

`-Xlint:all` 옵션을 추가해서 컴파일해보면 `로 타입` 사용 경고를 포함해 많은 경고가 나온다. <br>
이 코드는 `런타임`에 `ClassCastException`을 발생시킬 가능성도 매우 많다.

```java
Set<String> set1 = Set.of("오", "예", "스");
Set<Integer> set2 = Set.of(1, 2, 3);
Set unionSet = SetUtils.union(set1, set2);

for (Iterator it = unionSet.iterator(); it.hasNext(); ) {
    int x = (int) it.next(); // ClassCastException 발생
    System.out.println("x = " + x);
}
```

`1~3` 까지는 출력 되겠지만 문자인 원소를 만나는 순간 `예외가 발생`하게 될 것이다. <br>
이런 문제를 `컴파일 타임`에 `예방`할 수 있는 방법은 이 `메서드`를 `제네릭 메서드`로 만드는 방법이다.

```java
public static <E> Set<E> unionGeneric(Set<E> s1, Set<E> s2) {
    Set<E> result = new HashSet<>(s1);
    result.addAll(s2);
    return result;
}
```

코드가 의미하는 바는 바뀌지 않았지만, `제네릭 메서드`로 만드는 순간 효과는 굉장해진다.

```java
Set<String> set1 = Set.of("오", "예", "스");
Set<Integer> set2 = Set.of(1, 2, 3);
Set unionSet = SetUtils.unionGeneric(set1, set2); // Compile Error
```

`unionGeneric` `메서드`를 활용하면 다른 타입의 `Set`을 `병합`하려는 시도를 `원천봉쇄`할 수 있다. <br>
물론 `제네릭`을 활용하지 않고 `로 타입`의 `Set`을 넣어서 시도하면 `메서드 호출`까지는 가능하겠지만, <br>
`제네릭`을 활용하는 코드에서는 `컴파일 타임`에 실수로라도 `병합시도`를 하면 `컴파일 에러`가 발생한다.

`제네릭 메서드`의 작성 규칙은 `메서드` 내부에서 사용할 타입 매개변수 목록은 `접근 제어자`와 `반환타입` 사이에 와야한다.

- `제네릭 메서드`를 이용한 유틸리티 `불변객체` 사용

`제네릭 메서드`의 또 다른 장점은 `제네릭`은 `런타임`에 타입 정보가 `소거`된다. <br>
요청한 `타입 매개변수`에 객체의 타입을 바꿔줘야 하는데, 이 타입을 바꿔주는 `메서드`를 `정적 팩터리 메서드`라고 하고, `제네릭 싱글턴 팩터리  패턴`이라 부른다.

```java
private final static UnaryOperator<Object> IDENTITY_FN = t -> t;
```

예를 들어 간단한 `항등함수(Identity Function)` 을 구현한 클래스가 있다고 하면, `제네릭`이 만약 실체화 된다고 했을때는 `타입마다 하나씩` 만들어줘야 할 것이다.

`제네릭`은 `소거 방식`을 사용하고 있기 때문에 `하나면 충분`하다.

```java
private final static UnaryOperator<Object> IDENTITY_FN = t -> t;

    @SuppressWarnings("unchecked")
    public static <T> UnaryOperator<T> identityFunction() {
        return (UnaryOperator<T>) IDENTITY_FN;
    }
```

매번 요청한 타입에 맞게 `UnaryOperator<T>` 로 형변환해서 반환하면 되기 때문이다. <br>
이 형변환은 안전하지 않다는 경고가 발생하는데, `항등함수`는 입력 값을 수정하지 않기 때문에 `T`가 어떤 타입이던 `타입 안전`하다.

```java
String[] strings = { "삼베", "대마", "나일론" };
UnaryOperator<String> sameString = FunctionFactory.identityFunction();

for (String s : strings) {
    System.out.println(sameString.apply(s));
}

Number[] numbers = { 1, 2.0f, 3L };
UnaryOperator<Number> sameNumber = FunctionFactory.identityFunction();
for (Number n : numbers) {
    System.out.println(sameNumber.apply(n));
}
```

이렇게 다양한 타입을 이용해서 `항등함수`를 사용할 수 있는 것을 볼 수 있다. <br>
`제네릭`은 `컴파일 타임`에만 타입을 검사하고 컴파일 이후에는 `한정적 타입 매개변수`를 사용한 것이 아니라면 `Object`로 소거되기 때문이다.

이 패턴은 `Collections` 클래스에서 `emptySet` 이나 `reverseOrder` 메서드가 이렇게 구현되어 있다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/66b3109d-c295-4a54-b14f-154d36af6791)
![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/81dab556-feb8-4e83-ac13-01d6b82a1feb)

- `재귀적 타입 한정`을 이용한 `매개변수`의 허용 범위 한정

`재귀적 타입 한정`이란 자기 자신이 들어간 `표현식`을 사용하여 `타입 매개변수`의 허용 범위를 한정할 수 있다. <br>
`재귀적 타입 한정`은 주로 타입의 자연적 순서를 정하는 `Comparable 인터페이스`와 주로 함께 쓰인다.

```java
 static <E extends Comparable<E>> Optional<E> max(Collection<E> c)
```

위의 코드와 같이 `Comparable`을 구현한 경우에만 `타입 매개변수`로 허용하는 제한을 둔 것이다. <br>
`Comparable`은 인터페이스지만 `제네릭`에서는 `extends` 라는 키워드를 사용한다.

```java
class BoxNumber {

    int number;

    public BoxNumber(int number) {
        this.number = number;
    }

    
}

// java: method max in class RecursiveTypeBound cannot be applied to given types;
// required: java.util.Collection<E>
// found:    java.util.List<BoxNumber>
// reason: inference variable E has incompatible bounds
// equality constraints: BoxNumber
// lower bounds: java.lang.Comparable<E>
List<BoxNumber> numbers = List.of(new BoxNumber(1), new BoxNumber(3), new BoxNumber(20));
Optional<BoxNumber> max = max(numbers);
```

`Comparable`이 구현되어 있지 않다면, 이런 컴파일에러가 발생한다.

```java
class BoxNumber implements Comparable<BoxNumber> {

    int number;

    public BoxNumber(int number) {
        this.number = number;
    }

    @Override
    public int compareTo(BoxNumber o) {
        return Integer.compare(number, o.number);
    }
}
```

따라서 이렇게 `Comparable` 인터페이스를 구현해야 에러가 발생하지 않는다.

```java
static <E extends Comparable<E>> Optional<E> max(Collection<E> c) {
    // 예외를 던지는 대신 빈 Optional을 반환해주는 것이 좋다.
    if (c.isEmpty()) {
        return Optional.empty();
    }
    		
    E result = null;
    for (E e : c) {
        if (result == null || e.compareTo(result) > 0) {
            result = Objects.requireNonNull(e);
        }
    }
    		
    return Optional.of(result);
}
```

예제는 빈 컬렉션이 들어오면 `IllegalStateException`을 발생시키도록 작성 되어 있지만, 반환타입이 `E`가 아닌 `Optional<E>`로 변경하여 `안전성`을 한층 끌어 올렸다. <br>
`재귀적 타입 한정`은 훨씬 복잡해질 가능성이 있지만, 대부분 그런일은 일어나지 않는다.

- **정리**
1. 클라이언트에서 `매개변수`와 `반환값`을 명시적으로 `형변환`해야 하는 `메서드`보다 `제네릭 메서드`가 더 안전하고 사용하기에도 더 쉽다.
2. `형변환`을 해줘야 하는 기존 메서드는 `제네릭`하게 만들어야 한다.
