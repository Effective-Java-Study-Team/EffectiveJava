### 아이템 21 - 인터페이스는 구현하는 쪽을 생각해 설계하라

### 인터페이스에 메서드를 추가하는 방법

Java 8 버전 이전에는 기존 구현체를 깨뜨리지 않고는 인터페이스에 메서드를 추가할 방법이 없었다.

```java
public interface MiniCollection {

    void add();
    void remove();
    void newMethod();
}

public class Collection implements MiniCollection {
    @Override
    public void add() {
        System.out.println("add");
    }

    @Override
    public void remove() {
        System.out.println("remove");
    }
}

컴파일 에러 발생 -> 
Class 'Collection' must either be declared 
abstract or implement abstract method 'newMethod()' in 'MiniCollection'
```

인터페이스에 새로운 메서드를 추가하면, 보통은 `메서드를 구현하지 않았다는 컴파일 에러`가 발생하게 된다. <br>
우연히 메서드의 시그니처와 반환타입이 구현체와 겹친다면 `오버라이딩으로 취급`되어 컴파일은 되겠지만, 이런 경우는 거의 없기 때문이다.

Java 8 버전부터 추가된 `디폴트 메서드`를 선언하면 기존 인터페이스에 메서드를 추가할 수 있다.

```java
public interface MiniCollection {

    void add();
    void remove();

    default void newMethod() {
        
    }
}

디폴트 구현을 하면 메서드를 추가해도 구현체에 컴파일 에러가 발생하지 않는다.
```

디폴트 메서드를 선언해서 기존 인터페이스에 컴파일 에러 없이 메서드를 추가할 수 있지만, 이로 인해 모든 위험이 `사라진 것은 아니다.`

`디폴트 메서드`를 추가했을때 모든 `기존 구현체`들과 매끄럽게 연동되리라는 보장이 없다. <br>
Java 8 버전이 도입되면서 `핵심 컬렉션 인터페이스`들은 여러 `디폴트 메서드`들이 추가되었다.

물론 자바 라이브러리의 `디폴드 메서드`는 `코드 품질`이 높고 `범용적`이라 대부분 문제가 발생하지 않았다. <br>
하지만 모든 상황에서 `불변식`을 해치지 않는 `디폴트 메서드`를 작성하는 것은 어렵다.

Java 8 버전의 `Collection 인터페이스`에는 여러 `메서드`들이 추가 되었다.

```java
default boolean removeIf(Predicate<? super E> filter) {
    Objects.requireNonNull(filter);
    boolean removed = false;
    final Iterator<E> each = iterator();
    while (each.hasNext()) {
      if (filter.test(each.next())) {
    		 each.remove();
    		 removed = true;
      }
    }
    return removed;
}
```

대표적으로 `removeIf` 메서드가 있는데, `Predicate` 타입의 `filter` 를 인자로 받아서 `true` 를 반환하면 반복자의 `remove 메서드` 를 호출해 원소를 제거하도록 구현되어 있다.

대부분의 `Collection` 인터페이스의 구현체에서는 범용적으로 사용할 수 있도록 잘 구현되어 있다고 볼 수 있다. <br>
하지만 대부분의 `Collection` 인터페이스의 구현체에서 사용가능 하더라도, 모든 `Collection` 인터페이스의 구현체에 맞아 떨어지게 구현되었다고 볼 수 없다.

대표적으로 `아파치 커먼즈` 라이브러리와 `java.util` 패키지에서 제공하는 `SynchronizedCollection 동기화` 컬렉션 구현체들과 잘 어우러지지 못한다.

`removeIf` 메서드는 애초에 동기화를 고려하지 않고 만들어졌기 때문이다. <br>
`SynchronizedCollection` 인스턴스를 여러 스레드가 공유하게 되면 동기화가 되지 못해서 `CurrentModificationException`이 발생하거나 문제가 생길 수 있다.

### Java 에서 디폴트 메서드 추가로 인한 문제를 해결한 방법

`removeIf` 메서드의 경우 `동기화`를 고려하지 않고 만들어졌기 때문에 동기화 컬렉션에서 문제를 일으키게 된다. <br>
따라서 추가된 `디폴트 메서드를 재정의`하고, 다른 메서드에서 디폴트 메서드를 호출하기 전에 `전처리 작업`을 해준다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/bdf82f75-327f-42d0-bbef-9a11b92fd15b)

`디폴트 구현`을 호출하기 전에 `동기화`를 하도록 작성해서 문제를 해결했다.

`자바 플랫폼`에 속하지 않은 `컬렉션 구현체(아파치 커먼즈 라이브러리와 같은)`들은 이런 변화에 대응할 기회가 없었기 때문에, 활발한 `라이브러리`의 경우 수정을 했지만 아직도 수정되지 않고 있는 `라이브러리`도 많다.

디폴트 메서드는 기존 구현체에 `런타임 오류`를 일으킬 가능성도 있다. <br>
흔하게 일어나는 일은 아니지만, 기존 구현체의 `내부 구현`에 따라서 `디폴트 메서드`의 영향으로 인해 문제를 일으킬 수 있다.

### 디폴트 메서드는 조심히 추가해야 한다.

기존 인터페이스에 `디폴트 메서드`로 새 메서드를 추가하려면 `기존 구현체`들과 충돌이 없을지 매우 신중히 고민해야 한다.

`디폴트 메서드`를 새로 만드는 인터페이스에서 정의하려고 하는 경우라면, `표준 메서드`를 정의하는 데 아주 좋은 수단이기 때문에 `오히려 권장`한다.

`디폴트 메서드`를 사용하는 이유는 `메서드를 제거`하거나 기존 메서드의 `시그니처를 수정`하는 용도로 사용하려는 것이 아니다. <br>
이런 목적으로 `디폴트 메서드`를 추가해서 `인터페이스`를 변경한다면 이 `인터페이스`를 사용하는 `클라이언트`는 여러 문제에 봉착할 것이다.

`디폴트 메서드`라는 좋은 도구가 생겼더라도, `인터페이스 설계`시 세심한 주의를 기울어야 한다는 것이다. <br>
`심각하게 잘못된 인터페이스`는 이를 포함한 API에 어떤 문제를 일으킬지 예상조차 할 수 없다.

### 인터페이스를 릴리스하기 전이라면..

새로운 인터페이스를 릴리스하기 전이라면 반드시 `철저한 테스트`를 거쳐야 한다. <br>
여러 개발자들이 나름의 방식대로 인터페이스를 구현할 것을 예상하고 `최소한 세 가지 방식`으로 구현해보고 테스트 해야한다.

이렇게 섬세한 테스트를 통해 `인터페이스`를 `릴리스하기 전`에 결함을 찾을 기회를 얻을 수 있다. <br>
물론 `릴리스한 후`에도 `결함`을 수정하는 것이 가능한 경우도 있을 것이지만, 그때는 너무 늦을 수 있다.

여러 `클라이언트`들이 이미 해당 `인터페이스`에 의존하고 있을 수 있기 때문이다. <br>
따라서, 섬세한 테스트로 `릴리스하기 전` 결함을 찾아내어 수정하는 것이 가장 좋은 길이며 절대 `릴리스한 후`에 결함이 발견되면 수정이 가능할 수 있다는 가능성에 기대서는 안된다.