### 아이템 32 - 제네릭과 가변인수를 함께 쓸 때는 신중하라

### 제네릭과 가변 인수

`가변인수`와 `제네릭`은 자바 5 때 함께 추가되었다. <br>
`제네릭`과 `가변인수`는 서로 잘 어우러질 거 같지만 현실은 전혀 그렇지 않다.

`가변인수`는 `메서드에 넘기는 인수의 개수`를 클라이언트가 `조절`할 수 있게 해주는데, 구현 방식에 문제가 있다. <br>
`가변인수 메서드`를 호출하면 가변인수를 담기 위한 `배열이 만들어진다.`

이 배열은 `내부로 감춰야 하는 배열`이지만 클라이언트에게 노출하는 문제가 발생해버렸다. <br>
가변인수 `매개변수`에 `제네릭`이나 `매개변수화 타입`이 포함되면 컴파일 경고가 발생한다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/6f87a6ae-6692-446f-9bf6-7bcbc9429575)

이 경고는 `가변인수 매개변수`가 `실체화 불가 타입`이거나 `가변인수`를 사용하는 `메서드를 호출`할때 매개변수가 <br>
`실체화 불가 타입`으로 추론된다면 이 경고를 낸다.

```java
static void dangerous(List<String>... stringLists) {
    List<Integer> integerList = List.of(42);
    Object[] objects = stringLists;
    objects[0] = integerList; // Heap Pollution 
    String s = stringLists[0].get(0); // ClassCastException
}
```

이 `메서드`를 보면 `매개변수화 타입`의 리스트를 여러개 받을 수 있도록 `가변인수가 사용`되었다. <br>
`매개변수화 타입`으로 배열이 생성되면 안되지만, `가변인수`를 `매개변수`로 사용하면 배열이 생성된다.

따라서 List<String> 타입의 배열이 만들어지는 것이다. <br>
이 배열 인스턴스는 `공변`에 의해 `Object 타입`의 배열로 가리킬 수 있고, `Object 타입`의 배열로 취급되기 때문에  `integer 타입` 의 `List`가 배열 원소로 들어가게된다.

그렇게 되면 원래 가변인수 배열이 손상되어 `String` 타입의 원소를 꺼낼때 `예외가 발생`하게 되는 것이다.

이런 문제가 생기는데도 왜 `제네릭 배열`을 `varargs 매개변수`를 받는 메서드를 선언할 수 있게 했을까? <br>
이유는 간단하다 `제네릭`이나 `매개변수화 타입`의 `varargs 매개변수`를 받는 메서드가 실무에서 유용하게 쓰이고 있기 때문이다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/eb611216-769c-4b14-93ea-8428a6f31248)
![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/8183ade9-fde7-438b-8d5e-4334a44f44df)
![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/5ce1bcd0-9acf-4868-90e7-b834c41a4ab5)

`자바 라이브러리`에서도 이런 `메서드`들이 여럿 존재하고 있다. <br>
하지만 이 메서드들은 위에 작성했던 메서드들과 다르게 타입 안전하여 사용하는데 지장이 없다.

`자바 7 이전`에는 `@SuppressWarnings(”unchecked”)`와 같은 `애너테이션`을 달아서 가변인수에 대한 경고를 삭제할 수 있었지만, `다른 경고들까지 숨기는 문제`를 야기시켰다.

`자바 7 부터`는 `@SafeVarargs 애너테이션`이 추가되어서 `제네릭 가변인수 메서드` 작성자가 클라이언트 측에서 발생하는 `경고를 숨길 수 있게 되었다.` <br>
`@SafeVarargs` 역시 `@SuppressWarnings`를 사용할때와 마찬가지로 `메서드`가 안전한게 확실하지 않은 경우에는 `절대 사용해서는 안된다.`

그렇다면 어떻게 안전하다고 확신할 수 있을까? 두 가지만 확인해보면 된다.

- `메서드 내부`에서 배열에 아무것도 `저장`하지 않아야 한다.
- `배열의 참조`가 `밖으로 노출`되지 않아야 한다(`신뢰할 수 있는 코드`는 예외)

```java
static <T> T[] toArray(T... args) {
    return args;
}
```

큰 문제가 없어보이는 코드지만 이 코드는 문제가 있다. <br>
이 메서드가 반환하는 `배열의 타입`은 `컴파일 타임에 결정`된다. 그 시점에는 컴파일러에게 충분한 정보가 주어지지 않아서 `타입을 잘못 판단할 위험`이있다.

`varargs 매개변수 배열`을 그대로 반환하면 `힙 오염`을 이 `메서드`를 호출한 `콜스택으로까지 전이`하는 문제를 만들 수 있다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/6a86c7ef-49de-44c8-a1be-1020b8c0f618)

`Stack Area`는 `스레드`마다 새로 생성되어 개별적으로 사용하지만 `Heap`은 모든 `스레드`가 `전역적`으로 사용하게 된다. <br>
따라서 `varargs 매개변수 배열`을 그대로 반환하여, `Heap`을 오염시키면 이 `메서드`를 호출한 `콜스택`까지도 문제가 전파된다는 뜻으로 이해했다.

```java
static <T> T[] toArray(T... args) {
    return args;
}

static <T> T[] pickTwo(T a, T b, T c) {
    switch (ThreadLocalRandom.current().nextInt(3)) {
      case 0 : return toArray(a, b);
      case 1 : return toArray(a, c);
      case 2 : return toArray(b, c);
    }
    throw new AssertionError();
}

// ClassCastException 발생
String[] attributes = VarargsTest.pickTwo("좋은", "빠른", "저렴한");
```

이 코드를 보면 `toArray`에 넘길 `T 인스턴스` 2개를 담을 `가변인수 배열`을 만든다. <br>
`toArray`가 만드는 배열은 항상 `Object 타입의 배열`인데, 그로인해 `런타임`에 문제가 생길 수 있다.

### 제네릭과 가변 인수를 안전하게 사용하는 법

`제네릭`과 `가변인수`를 안전하게 사용하려면, <br>
`가변인수`를 담은 `제네릭 배열`에 `아무것도 담거나 덮어쓰지` 말고 `외부에 노출`하지 말라는 `경고`를 했었다.

하지만 두 가지 경우에는 이 경고를 지키지 않아도 안전하다.

- `@SafeVarargs`로 제대로 `애노테이트`된 다른 `varargs 메서드`에 넘기는 것은 안전하다.
- 배열 내용의 일부 함수를 호출만 하는(`varargs`를 받지 않는) `일반 메서드`에 넘기는 것도 안전하다.

```java
@SafeVarargs
static <T> List<T> flatten(List<? extends T>... lists) {
    List<T> result = new ArrayList<>();
    for (List<? extends T> list : lists) {
        result.addAll(list);
    }
    		
    return result;
}
```

이 메서드는 `제네릭 타입`의 `가변인수 배열`을 `수정`하거나 `외부에 노출`하지 않는다. <br>
따라서 이 메서드는 `타입 안전`하다. `컴파일타임`의 `경고`를 제거하기 위해 `@SafeVarargs`만 붙여주면 된다.

`@SafeVarargs` 만이 유일한 정답은 절대 아니다. <br> 
`제네릭 타입 가변인수`를 `List` 타입 `매개변수`로 변경해서 `컴파일 경고`의 `원인`을 `제거`할 수도 있다.

```java
static <T> List<T> flatten(List<List<? extends T>> lists) {
    List<T> result = new ArrayList<>();
    for (List<? extends T> list : lists) {
        result.addAll(list);
    }
    
    return result;
}
```

결과적으로 `경고를 제거한 메서드`를 사용하면 `코드가 간단`하고 `임의개수의 인수`를 넘길 수 있다는 장점이 있고, <br>
`제네릭 타입 가변인수`를 `List` 타입 `매개변수`로 변경하면 `컴파일 경고의 원인을 제거`할 수 있다.

단점으로는 `경고를 제거한 메서드`는 `@SafeVarargs` 애너테이션을 붙여야 하고 `메서드가 안전한지 증명`해야하고, <br>
`두 번째 메서드`는 `클라이언트 코드`가 살짝 지저분해지고 `속도`가 `조금` 느려질 수도 있다.

`제네릭 타입`의 배열을 반환하던 `pickTwo 메서드`도 `List`를 `반환`하도록 코드를 변경하면 `타입 안전`해진다.

```java
static <T> List<T> pickTwo2(T a, T b, T c) {
    switch (ThreadLocalRandom.current().nextInt(3)) {
      case 0 : return List.of(a, b);
      case 1 : return List.of(a, c);
      case 2 : return List.of(b, c);
    }
    throw new AssertionError();
}
```

- **@SafeVarargs 애너테이션에 대한 TMI**
    - `@SafeVarargs` 애너테이션은 `재정의할 수 없는 메서드`에만 달아야한다.
    - `자바 8`에서는 `정적 메서드`와 `final 인스턴스 메서드`에만 붙일 수 있었다.
    - `자바 9` 부터는 `private 인스턴스 메서드`에도 허용된다.

- **정리**
1. `가변인수와 제네릭`은 궁합이 좋지 않다.
2. `제네릭 varargs 매개변수`는 타입 안전하지는 않지만 허용된다.
3. `메서드`에 `제네릭 varargs 매개변수`를 사용하고자 한다면 메서드가 `타입 안전한지 확인`하고 `@SafeVarargs 애너테이션`을 붙여서 `컴파일 경고`를 제거하자.
