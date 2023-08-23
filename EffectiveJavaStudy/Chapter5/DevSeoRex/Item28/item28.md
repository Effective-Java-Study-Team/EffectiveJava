### 아이템 28 - 배열보다는 리스트를 사용하라

### 제네릭과 배열의 차이
배열과 제네릭의 차이는 `공변`이냐 `반공변`이냐의 차이에서 온다.

- **공변(covariant)**

```java
Super superObject = new Sub();

Super[] supers = new Sub[4];
```

이 코드 처럼 `Sub`가 `Super`의 하위 타입이라면 배열 `Sub[ ]` 또한 `Super[ ]`의 하위타입이다. <br>
이런 관계를 `공변(covariant)` 이라고 부른다.

- **불공변(invariant)**

```java
// 가능
Object object = new String("111");

// 불가능
List<Object> list = new ArrayList<String>();
```

String은 Object의 하위 타입이 맞지만, `List<Object>`는 `List<String>`의 `상위 타입`도 `하위 타입`도 아니다. <br>
이런 관계를 `불공변(intvaiant)` 이라고 부른다.

- **배열의 공변으로 인해 생기는 문제**

```java
Object[] objectArray = new Long[1];
// Exception in thread "main" java.lang.ArrayStoreException: java.lang.String
objectArray[0] = "타입이 일치하지 않아 넣을 수 없다!";
```

이 코드는 `공변`에 의해서 `컴파일 에러`가 발생하지 않는다. <br>
`Long` 타입의 배열에 `String` 타입의 원소를 넣으려고 하는 그 순간에 `런타임 에러`가 `발생`하게 된다.

`컴파일 타임`에 잡을 수 있는 `오류`가 가장 이상적인 `오류`인데, `런타임`에 알 수 있는 `오류`는 좋지 않다.

```java
List<Object> objectList = new ArrayList<Long>();
```

이 코드는 `컴파일 에러`로 인해 컴파일 조차 되지 않는다. <br>
배열을 사용하면 `런타임`에 이 문제를 알 수 있지만, `제네릭`을 사용하면 컴파일 타임에 문제를 알게 되는 것이다.

`제네릭`과 `배열`은 `공변과 불공변`의 차이 이외에도 한 가지의 차이점을 더 가지고 있다.

배열은 `실체화` 된다는 점이다. <br>
배열은 `런타임`에도 자신이 담기로 한 원소의 타입을 `인지하고 확인`한다는 의미이다. <br>
그렇기 때문에 `Long` 배열에 `String` 타입의 원소를 넣으려고 하면 `ArrayStoreException`이 발생하는 것이다.

`제네릭`은 타입 정보가 `런타임`에는 소거 되기 때문에 원소 타입을 `컴파일타임`에만 검사하며 `런타임`에는 알 수 없다.

이런 차이 때문에 `배열과 제네릭`은 조화롭게 `협동`할 수 없다. <br>
배열은 `제네릭` 타입, `매개변수화` 타입, 타입 `매개변수`로 사용하는 것이 불가능하다.

```java
static <T> T[] getArr1() { // -> Type parameter 'T' cannot be instantiated directly
    // 타입 매개변수를 배열로 사용 -> 에러
    return new T[20];
}

static <E> List<E>[] getArr2() { // -> Generic array creation
    // 배열의 제네릭 타입 사용 -> 에러
    return new ArrayList<E>[20];
}

static List<String>[] getArr3() { // -> Generic array creation
    // 매개변수화 타입 사용 -> 에러
    return new ArrayList<String>[30];
}
```

`제네릭 배열`을 생성하지 못하도록 막은 이유는 무엇일까? <br>
`제네릭`을 도입하게 된 이유인 `타입 안정성`을 해칠 가능성이 매우 높기 때문이다.

```java
// 컴파일 에러가 나는 코드지만 이 코드가 허용된다고 가정하면 큰 재앙이 몰려온다.
List<String>[] stringList = new List<Strring>[1];

// 정수형 리스트를 만든다.
List<Integer> intList = List.of(42);

// 배열의 공변에 의해 이 코드는 에러가 나지 않는다.
Object[] Objects = stringList;

// intList를 배열에 넣어준다 -> 런타임에는 List<Intger>가 로 타입이되기 때문이다.
objects[0] = intList;

// 이 코드에서 문제가 발생한다 -> Integer -> String 형변환은 불가능하기 때문이다.
String s = stringList[0].get(0);
```

첫 번째 줄의 코드를 허용하게 되면 이런 문제가 발생하게 되는 것이다. <br>
그렇기 때문에 자바에서 `제네릭 배열을 생성`하는 것을 `문법적으로 금지`하고 있다.

### 제네릭 컬렉션 → 자신의 원소 타입을 담은 배열(우회 해서 가능!)

`E`, `List<E>`, `List<String>`과 같은 타입을 `실체화 불가 타입`이라고 말한다. <br>
실체화되지 않아서 `런타임`에는 `컴파일타임`보다 `타입 정보`를 적게 가지는 타입이다.

`소거 메커니즘`을 활용하기 때문에 `매개변수화 타입`에서 `실체화될 수 있는 타입`은 비한정적 와일드카드를 이용한 타입밖에 없다. <br>
`비한정적 와일드 카드`를 이용하면 `컴파일러의 요소`가 실제 타입 정보를 알 필요가 없기 때문이다.

```java
List<?>[] arr = new List<?>[20];
arr[0] = new ArrayList<String>();
arr[1] = new ArrayList<Integer>();
```

`어떤 타입`이던지 전부 받아들일 수 있기 때문에, `컴파일러의 요소`가 `실제 타입정보`를 알 필요가 없는 것이다.

- **제네릭 컬렉션 → 자신의 원소 타입을 담은 배열**

`제네릭 컬렉션` 에서는 `자신의 원소 타입`을 담은 배열을 반환하는게 보통은 불가능하다.

```java
List<String> stringList = List.of("1", "2", "3");

// class [Ljava.lang.Object; cannot be cast to class [Ljava.lang.String;
// ([Ljava.lang.Object; and [Ljava.lang.String; are in module 
// java.base of loader 'bootstrap')
String[] array = (String[]) stringList.toArray();
```

List의 toArray 메서드는 Object 배열을 반환한다. <br>
`Object` 배열은 `String` 배열로 형변환 가능하지만, `제네릭 컬렉션`의 메서드는 타입 정보를 소거한 후 `Object` 배열을 반환하기 때문에 에러가 발생한다.

```java
String[] array = stringList.toArray(new String[0]);
```

`List`에서 제공하는 `제네릭 메서드`를 사용하면 이런 문제를 피할 수 있다.

이런 방식을 `Super-Type-Token` 방식이라고 말한다. <br>
`클래스 리터럴`을 사용해서 `제네릭 타입 정보`를 유지하는 방법이다.

```java
static <T> T[] toArray(List<T> list, Class<T> clazz) {
    @SuppressWarnings("unchecked")
    T[] array = (T[]) Array.newInstance(clazz, list.size());
    		
    for (int i=0; i<list.size(); i++) {
        array[i] = list.get(i);
    }
    
    return array;
}
```

이런식으로 `메서드`를 작성하면, `반환 받을 배열`의 `타입을 지정`할 수 있다는 장점이 있어, `타입 안전`하다.

```java
String[] array = toArray(stringList, String.class);
```

호출도 `형변환` 없이 바로 가능하고, `컴파일`시에 컴파일러가 자동으로 `형변환 코드를 삽입`해준다.

`제네릭 타입`과 `가변인수 메서드` 또한 조합이 좋지 않다.

```java
@SafeVarargs
static void dangerous(List<String>... args) {
    Object[] listArr = args;
    listArr[0] = List.of(1);
    String s = args[0].get(0);
}

// ClassCastException 발생
dangerous(List.of("1", "2", "3", "4", "6"), List.of("2"), List.of("3"));
```

`가변인수 메서드`를 호출할 때마다 `가변인수 매개변수`를 담을 배열이 하나 만들어진다. <br>
이때 배열의 원소가 `실체화 불가 타입`이라면 경고가 발생하게 된다.

원래는 만들 수 없지만, `장점`이 매우 많기 때문에 예외적으로 `모순을 허용`하고 있는 것이다. <br>
`dangerous` 메서드는 호출하면 예외가 발생한다.

`args` 배열을 `Object 타입` 배열로 참조하고, 첫 번째 원소로 `Integer 배열`을 넣어버렸기 때문이다. <br>
그때 `String 타입`으로 `args` 를 참조해서 값을 가져오려고 하면 `형변환 오류`가 발생하는 것이다.

`@SupperessWarning` 과 같이 안전하다고 검증된 메서드에만 `@SafeVarags` 애너테이션을 붙일 수 있다. <br>
안전하지 않은 곳에 붙인다면, 문제를 숨겨서 보안 위협만 더 크게 만들 뿐이다.

위의 메서드는 안전하지 않다. <br>
그렇다면 어떻게 하면 `실체화 불가 타입`의 가변 인수 메서드를 안전하게 사용할 수 있을까?

두 가지만 지키면 된다.

1. `varargs` 매개변수 배열에 아무것도 저장해서는 안된다.
2. `varargs` 배열 또는 그 복제본을 `신뢰할 수 없는 코드`에 노출해서는 안된다.

```java
@SafeVarargs
static <T> List<T> flatten(List<? extends T>... lists) {
    List<T> result = new ArrayList<>();
    for (List<? extends T> list : lists)
        result.addAll(list);
    return result;
}
```

두 가지 조건을 잘 지킨 메서드 예시이다.
`매개변수 배열`을 `수정`하거나, `새로운 값`을 넣지 않고 있으며 `신뢰할 수 없는 코드`에 매개변수 배열을 제공하거나 `복사본을 제공`하지도 않는다.

### 비검사 형변환 경고는 배열 대신 컬렉션을 사용해 해결하자

배열로 형변환을 수행할때 `제네릭 배열 생성 오류`나 `비검사 형변환 경고`를 만나는 경우가 있다. <br>
대부분은 `E[ ]` 대신 `List<E>` 를 사용하면 해결할 수 있다.

```java
public class Chooser {

    private final Object[] choiceArray;

    public Chooser(Collection choices) {
        this.choiceArray = choices.toArray();
    }

    public Object choose() {
        Random rnd = ThreadLocalRandom.current();
        return choiceArray[rnd.nextInt(choiceArray.length)];
    }
}
```

`컴파일`에는 아무런 문제가 없는 코드다.

이 코드의 가장 큰 문제는 `Object 타입의 배열`이므로 `모든 타입의 원소`를 `허용`하고 있다는 점이다. <br>
다른 타입의 원소가 들어있는데 `타입 캐스팅`을 시도한다면 `예외가 발생`할 것이다.

```java
public class Chooser<T> {

    private final T[] choiceArray;

    @SuppressWarnings("unchecked")
    public Chooser(Collection<T> choices) {
        this.choiceArray = (T[]) choices.toArray();
    }

    public Object choose() {
        Random rnd = ThreadLocalRandom.current();
        return choiceArray[rnd.nextInt(choiceArray.length)];
    }
}
```

`제네릭`을 활용해 이렇게 코드를 변경한다면 타입 안전해질 수 있다. <br>
`@SuppressWarning`을 붙여주면 `비검사 경고`를 `해제`할 수 있다. 지금 이 메서드는 `타입 안전이 검증`되었기 때문에 경고를 제거해도 괜찮다.

하지만 이 코드는 경고를 제거하고, 주석으로 안전하다는 코멘트를 전달할 수 있겠지만 <br>
경고의 원인을 제거할 수 있는 방법이 있기 때문에 이 경우에는 `경고의 원인을 제거`하는 것이 더 좋다.

```java
public class RefactorChooser<T> {

    private final List<T> choiceList;

    public RefactorChooser(Collection<T> choices) {
        this.choiceList = new ArrayList<>(choices);
    }

    public T choose() {
        Random rnd = ThreadLocalRandom.current();
        return choiceList.get(rnd.nextInt(choiceList.size()));
    }
}
```

배열 대신 리스트를 쓰면 `비검사 형변환 경고`를 제거할 수 있다. <br>
물론 `코드양`이 조금 늘고, `속도`도 조금 더 느릴 것이다. 하지만 `런타임`에 `ClassCastException`이 일어날 확률은 없으니 `그만한 가치`가 있다고 할 수 있다.

- **정리**
1. 배열은 `공변`이고 `실체화`되는 반면, `제네릭은 불공변`이고 타입 정보가 `소거`된다.
2. 배열은 `런타임`에는 `타입 안전`하지만 컴파일 타임에는 그렇지 않다.
3. `제네릭과 배열`을 섞어 쓰다가 컴파일 오류나 경고를 만난다면 `배열을 리스트로` 바꿔보자.
