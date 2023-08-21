### 아이템 26 - 로 타입은 사용하지 말라

### 제네릭과 로 타입 매개변수화 타입

`자바 5`부터 추가된 `제네릭`은 `Type-Safe`한 개발을 도와준다. <br>
`제네릭`을 사용하면 `컴파일 타임`에 `컬렉션`에 `엉뚱한 타입`의 객체를 넣지 못하도록 `차단`한다.

`클래스`와 `인터페이스` 선언에 `타입 매개변수`가 쓰이면 이를 `제네릭 클래스` 혹은 `제네릭 인터페이스`라고 한다.

```java
// 제네릭 클래스
public class Box<T> {}

// 제네릭 인터페이스
public interface Set<E> {}
```

`제네릭 클래스`와 `제네릭 인터페이스`를 통틀어서 `제네릭 타입` 이라고 부른다.

- **매개변수화 타입**

`매개변수화 타입`이란, `클래스` 또는 `인터페이스`의 이름이 나오고 `꺾쇠괄호` 안에 `실제 타입 매개변수`를 나열하는 것을 말한다.

```java
List<String> strings = new ArrayList<String>();
```

`List<String>` 에서 원소의 타입인 `String`이 `타입 매개변수 E`에 해당하는 실제 타입 매개변수이다.

```java
public class FakeCollection<E> {
    
    private List<E> list = new ArrayList<>();
    
    public void add(E e) {
        list.add(e);
    }
    
    public boolean contains(E e) {
        return list.contains(e);
    }
}
```

여기서 `E`가 `타입 매개변수`를 뜻한다. <br>
`제네릭`은 하위 버전과의 `호환성 유지`를 위해서 `제네릭 구현`에는 `소거`를 사용하고 있는데, 예시를 보면 이렇다.

```java
FakeCollection<String> collection = new FakeCollection<>();
```

`FakeCollection`의 타입 매개변수를 `String`으로 지정하면, `E`로 표시된 `타입 매개변수`가 전부 `치환`되어 사용된다.

```java
public class FakeCollection<String> {
    
    private List<String> list = new ArrayList<>();
    
    public void add(String e) {
        list.add(e);
    }
    
    public boolean contains(String e) {
        return list.contains(e);
    }
}
```

`컴파일` 이후에는 `타입 매개변수`가 `실제 타입`으로 치환되기 때문에, 위의 코드와 같이 코드가 바뀐다.

- **로 타입**

`List<E>`는 `제네릭 타입`이고, `List`는 `로 타입`에 해당한다. <br>
`로 타입`은 `제네릭 타입 정보`가 전부 지워진 것처럼 동작한다. `제네릭`이 만들어지기 전에 작성된 코드와의 호환성 유지를 위해 `어쩔 수 없는 선택`이였다.

```java
// 이 컬렉션은 숫자만 넣을 수 있다.
Collection collection = new ArrayList();

// 숫자를 넣는다.
collection.add(1);
// 실수로 문자를 넣는다.
collection.add("1");

for (Iterator i = collection.iterator(); i.hasNext();) {
    Integer number = (Integer) i.next(); // ClassCastException이 발생한다.
}
```

`로 타입`의 가장 큰 문제는, `어떤 타입`이 들어가도 된다는 것이다. <br>
`숫자`만 넣을 수 있다는 주석을 보지 못하고, 이 로 타입 컬렉션에 `문자`를 넣으면 넣을때는 아무 문제가 없다.

하지만 `Iterator`를 활용해서 `원소를 순회`할때 `Integer`로 형변환을 시도하면 `런타임`에 문제가 발생한다.

`에러`는 `컴파일 타임`에 잡을 수 있는 것이 가장 처리하기 쉽고 `추적하기에도 용이`하다. <br>
`런타임`에 문제를 겪는 코드는 원인을 제공한 코드가 `물리적으로 상당히 떨어져 있을 가능성`이 크다.

숫자만 넣을 수 있다는 주석은 이 코드를 사용하는 클라이언트에게 숫자 이외의 타입을 넣을 수 없도록 강제하지 못한다. 그것이 가장 큰 문제다.

```java
// 이 컬렉션은 숫자만 넣을 수 있다.
Collection<String> collection = new ArrayList<>();

// 숫자를 넣는다.
collection.add(1);
// 실수로 문자를 넣는다 -> 컴파일 에러 발생
collection.add("1");
```

`제네릭`을 활용하면 `컴파일 타임`에 허용되지 않는 타입을 컬렉션에 넣지 못하도록 컴파일 에러를 내준다.

또한 `컴파일러`는 컬렉션에서 원소를 꺼낼때 모든 곳에 `보이지 않는 형변환`을 `추가`해준다. <br>
직접 `형변환` 할 필요도 없고, 절대 이 형변환이 `실패하지 않음을 보장`해주기에 `안전`하기까지 한 것이다.

`로 타입`은 아직까지 사용할 수 있지만, `제네릭의 안정성과 표현력`을 모두 잃게 되기 때문에 절대 사용해서는 안된다.

`로 타입`은 많은 문제를 일으키지만 자바가 `제네릭`을 받아들이는 데 `거의 10년`이 걸려버렸기 때문에 `제네릭` 없이 짠 코드가 이미 많은 곳에서 사용되었다. <br>
`기존 코드`를 모두 수용하면서 `제네릭`을 사용할 수 있게 하는 대안으로 `로 타입`을 사용하는 메서드에 `매개변수화 타입`의 인스턴스를 넘겨도 동작하도록 해야했다.

물론 그 반대인 `매개변수화 타입`을 사용하는 `메서드`에 `로 타입의 인스턴스`를 넘겨도 동작한다.

```java
List list = new ArrayList();
List<String> list2 = new ArrayList<>();

void doSomething(List list) {}
void foo(List<String> list) {}

doSomething(list2);
doSomething(list);
```

위의 코드처럼 `로 타입`을 사용하는 `메서드`에 `매개변수화 타입` 인스턴스를, <br>
`매개변수화 타입`을 사용하는 `메서드`에 로 타입 `인스턴스`를 넘겨도 정상 동작해야 한다는 뜻이다.

`매개변수`를 `로 타입`으로 받으면 모든 매개변수화 타입을 받을 수 있지만, <br>
`List<Object>`는 `List<String>`을 받을 수 없다.

왜 그럴까?

`List<String>`은 `List`의 `하위 타입`이지만 `List<String>`은 `List<Object>`의 `하위 타입`이 아니다.

- **배열과 제네릭 타입의 차이**

배열은 `공변`이지만 제네릭은 `불공변`이다. <br>
공변이란 `Sub`가 `Super`의 `하위 타입`이라면 `Sub[ ]도 Super[ ]의 하위 타입`이 된다.

불공변이란 `List<Sub>`는 `List<Super>`의 하위 타입도 아니고, 상위 타입도 되지 않는다.

```java
// ArrayStoreExcetpion 발생!
Object[] arr = new Long[1];
arr[0] = 1;

// Compile Error 발생!
List<Object> list = new ArrayList<Long>();
```

배열은 `공변`이기 때문에 `런타임`에 `ArrayStoreException`을 발생시키지만, <br>
`제네릭`은 `불공변`이기 때문에 컴파일 타임에 하위 타입이 아니라는 `컴파일 에러`를 내뱉는다.

```java
public static void main(String[] args) {
    List<String> strings = new ArrayList<>();
    unsafeAdd(strings, Integer.valueOf(42));
    String s = strings.get(0); // 컴파일러가 자동으로 형변환 코드를 넣어준다 -> 묵시적 형변환
}

private static void unsafeAdd(List list, Object o) {
    list.add(o);
}

private static void unsafeAdd(List<Object> list, Object o) {
    list.add(o);
}
```

`unsafeAdd` 메서드를 보면 하나는 `로 타입`을 받고, 하나는 `제네릭`을 사용한다. <br>
여기서 숫자 42를 넣게 되면 `List<String>`에서 원소를 꺼내면 자동으로 `String으로 형변환`하므로, 문제가 생긴다.

`List<Object>` 타입의 매개변수를 받는 메서드를 호출하면 `컴파일` 조차 되지 않는다. <br>
이 예제로 알 수 있듯이 `로 타입`은 `위험천만`하고, 사용해서 얻는 `이익이 없다.`

### 비한정적 와일드 카드

```java
static int numElementsInCommon(Set s1, Set s2) {
    int result = 0;
    for (Object o1 : s1) {
      if (s2.contains(o1)) result ++;
    }

    return result;
}
```

이 메서드는 동작하는데 아무 문제가 없다. <br>
하지만 `로 타입`을 사용해서 안전하지 않다는 문제가 있다.

따라서 `비한정적 와일드카드 타입`을 대신 사용하는 것이 좋다. <br>
어떤 타입이 들어와야 하는지 신경쓰고 싶지 않다면 로 타입 대신 `비한정적 와일드 카드`를 사용하자.

```java
static int numElementsInCommon(Set<?> s1, Set<?> s2) {
    int result = 0;
    for (Object o1 : s1) {
      if (s2.contains(o1)) result ++;
    }

    return result;
}
```

`로 타입 컬렉션`은 아무 원소나 넣을 수 있기 때문에 `타입 불변식`을 `훼손`할 수 있다. <br>
`Collection<?>`에는 `null` 이외에는 아무런 원소도 넣을 수 없는 제약이 발생한다. 다른 원소를 넣으면 컴파일시 오류가 발생한다는 뜻이다.

```java
Collection<?> collection = new ArrayList<>();
// java: incompatible types: java.lang.Object cannot be 
// converted to capture#1 of ?
collection.add("1"); -> 컴파일 에러 발생
collection.add(null);
```

이런 문제가 발생하는 이유는 `Collection` 인터페이스를 보면, `E`라는 `타입 매개변수`를 이용하고 있다. <br>
비한정적 와일드 카드를 사용하게 되면, 이 컬렉션에 어떤 타입이 들어갈 수 있는지 컴파일러는 알 수 없다.

`타입을 확정`해줄 수 없기 떄문에 생기는 `컴파일 에러`이다. <br>
`와일드 카드`를 사용하면 `capture of`를 내부적으로 호출하여 타입을 지정해야 하기 때문이다.

이런 문제를 해결하기 위해서는 `한정적 와일드 카드 타입`이나, `제네릭 메서드`를 사용하면 된다.

```java
static void add1(Set<?> set, Object o) {
    set.add(o);
}
    
static void add2(Set<? super Object> set, Object o) {
    set.add(o);
}
    
static <E> void add3(Set<E> set, E e) {
    set.add(e);
}
```

첫 번째 메서드만 컴파일 에러가 난다. <br>
그 이유는 두 번째 메서드는 `어떤 타입`이 들어올 수 있는지 `명시한 것`은 아니지만, `Object와 Object의 자손들`만 들어올 수 있다고 `한정`되어 있다.
 
세 번째 메서드는 `제네릭`을 이용한 `타입 변수`를 사용하여 `타입을 확정`해주고 있기 때문에 문제가 발생하지 않는다. <br>
첫 번째 메서드는 어떤 타입이 들어올 수 있는지 `타입을 확정`해 줄 수 없기 때문에 이런 `문제가 발생`한다.

```java
private static void foo(List<?> i) {
// 에러 발생 -> java: incompatible types: java.lang.Object cannot be converted to capture#1 of ?
//        i.set(0, i.get(0));
    doSomething(i);
}

private static <T> void doSomething(List<T> i) {
    i.set(0, i.get(0));
}
```

이런 문제를 해결하기 위해서, `같은 일을 하는 메서드`를 `제네릭 타입`을 활용하도록 작성하고 `도우미 메서드`로     `내부 호출` 해 사용하면 이런 문제를 해결할 수 있다.

- **로 타입을 사용해야 하는 케이스 두 가지**

무조건 로 타입을 사용하지 않는 것을 권장하지만, 써야 하는 두 가지 케이스가 있다. <br>
쉽게 생각해서 두 가지 케이스 이외에는 로 타입을 쓰는 것을 절대 권장하지 않는다는 이야기가 된다.

먼저 `class 리터럴`은 `로 타입`을 써야한다.

```java
List.class
String[].class
int.class

// 허용하지 않는다.
List<String>.class
List<?>.class
```

`자바 명세`에서는 `class 리터럴`의 경우 배열과 기본 타입은 허용하지만 `매개변수화 타입`은 허용하지 않는다.

두 번째 케이스는 `instanceof` 검사를 할때 로 타입을 사용해야한다. <br>
`런타임`에는 `제네릭 타입`의 정보가 모두 지워지므로, 비한정적 와일드 카드 타입 이외의 `매개변수화 타입`에는 `instanceof 연산자`를 적용할 수 없다.

```java
if (o instance of Set<String>) {
    // 컴파일 에러 발생
}

if (o instance of Set(Set<?>)) {
    // 에러 발생 안함
    Set<?> s = (Set<?>) o;
}
```

`로 타입`과 `비한정적 와일드카드 타입`은 `instanceof` 연산이 완벽히 동작한다. <br>
다만 비한정적 와일드카드 타입의 `꺾쇠괄호와 물음표`는 아무 역할도 없이 `코드만 더럽히므로` 로 타입을 쓰는 것이 깔끔하다.

`instanceof` 타입 검사는 `로 타입`으로 하고, `형변환`에는 `비한정적 와일드카드 타입`을 쓰는 것이 좋다.

- **정리**
1. `로 타입`을 사용하면 `런타임에 예외`가 일어날 수 있으니 사용하면 `안된다.`
2. `Set<Object>` 매개변수화 타입이고, `Set<?>`는 모종의 타입 객체만 저장할 수 있는 `와일드 카드 타입`이다.
3. `매개변수화 타입`과 `와일드 카드 타입`은 안전하지만 `로 타입`은 안전하지 않다.


![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/b108f460-3717-4f66-b076-b27b391b717a)


    
