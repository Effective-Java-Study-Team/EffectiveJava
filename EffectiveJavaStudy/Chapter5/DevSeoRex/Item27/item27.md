### 아이템 27 - 비검사 경고를 제거하라

### 비검사 경고가 발생하는 이유
`제네릭`을 사용하다 보면 수많은` 컴파일러 경고`가 발생하게 된다. <br>
대표적으로 `rawtypes & unchecked` 경고가 있다. 

조금 더 세분화 시켜서 보면, <br>
`비검사 형변환 경고`, `비검사 메서드 호출 경고`, `비검사 메서드 매개변수화 가변인수 타입 경고`, `비검사 변환 경고` 등이 있다.

```java
Set<String> set = new HashSet();
```

이 코드는 정상적으로 작동한다. <br>
동작에는 문제가 없지만, 즉 `컴파일 에러`는 아니지만 문제가 생길 수 있는 요소를 알려주는 것이 `컴파일러 경고`다.

경고는 `컴파일 에러`가 아니기 때문에 항상 출력되지 않아서 따로 `javac 명령줄 인수`에 `옵션`을 `추가`해야 볼 수 있다. <br>
경고를 보는 방법에는 여러가지 방법이 있는데 크게 세가지가 있다.

- **javac -Xlint:all <source files>**

`로 타입`을 사용하는 경고와 `비검사 형변환 경고`가 동시에 발생하는 케이스라면, `하나의 옵션`만 지정하면 그 경고만 출력되는 문제가 발생한다. <br>
이런 경우에는 `어떤 경고가 출력`될지 확신이 서지 않는다면, `all 옵션`을 줘서 모든 경고를 확인할 수 있다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/f87e7167-494c-459a-b426-3668bc49c570)

- **javac -Xlint:<option1, option2..> <source files>**

모든 경고를 출력하면 너무 많은 경고가 나오고, `핵심 경고`를 놓칠 수 있으므로 <br>
`보고 싶은 경고`의 유형만 옵션으로 지정해서 볼 수 있는 방법이다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/3822bd8d-3224-43b8-9066-df1b09357425)

- **javac -Xlint:none <source files>**

`경고를 보지 않는 옵션`이다. <br>
`-Xlint 옵션`을 주지않고 `컴파일`하는 것과 같은 동작을 한다. 콘솔에는 `경고`가 있으니 `확인`해보라는 `메시지`가 출력된다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/b7c596fe-480e-44f8-bf46-94a6c120fc30)

### 비검사 경고를 제거해야 하는 이유

`비검사 경고`를 제거하면 `런타임`에 `ClassCastException`이 발생할 일이 없다고 확신할 수 있다. <br>
경고가 제거되지 않는 케이스에서는 `타입 안전`하다는 확신이 들면 `@SuppressWarning` 애너테이션으로 경고를 숨기자.

타입 안전함을 검증하지 않고 경고를 숨기면 `잘못된 보안 인식`을 심어주게 되기 때문에 `조심해야한다.` <br>
`안전하다고 검증된 경고`를 숨기지 않으면 `진짜 중요한 경고`를 보지 못할 수 있기 때문에 검증된 경고는 숨겨야 한다.

`@SuppressWarning` 애너테이션은 좁은 범위에 적용해야 한다. <br>
`변수`, `메서드`, `타입` 어디에도 붙일 수 있지만 클래스 전체에 적용하는 것은 절대 좋지 않다.

한 줄이 넘는 `생성자나 메서드`라면 `지역 변수`를 활용해서 최대한 적은 범위에 `@SuppressWarning`의 영향이 가도록 해야한다. <br>
`return 문`에는 `@SuppressWarning`을 사용할 수 없기 때문에 `지역 변수`를 주로 활용한다.

```java
private static <T,U> T[] copyOfRange(U[] original, int from, int to, Class<? extends T[]> newType) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);

        // T[ ] 타입의 하위 타입만 메서드에서 사용되므로 타입 안전함이 보장되므로 경고를 제거한다.
        @SuppressWarnings("unchecked")
        T[] copy = ((Object)newType == (Object) Object[].class)
                ? (T[]) new Object[newLength]
                : (T[]) Array.newInstance(newType.getComponentType(), newLength);
        System.arraycopy(original, from, copy, 0,
                Math.min(original.length - from, newLength));
        return copy;
    }
```

이 메서드도 `@SuppressWarnings` 애너테이션을 사용하지 않으면 비검사 경고가 발생한다. <br>
하지만 이 메서드는 `T[ ]` 타입으로 `형변환`이 가능하다는 것을 이미 보장받고 있으므로, `경고를 제거`해야 한다.

`T[ ] 타입의 하위 타입`이 `newType`으로 메서드에서 사용되기 때문에 `T[ ]` 타입으로 `형변환`하는 것은 절대 실패하지 않는다.

다만 `System.arraycopy`를 사용하는 부분에서 `original 배열`과 `copy 배열`의 타입의 차이로 인해 문제가 발생하는 것은 막을 수 없다.

```java
String[] arr = {"1", "2"};

// arraycopy: type mismatch: can not copy java.lang.String[] into java.lang.Integer[]
Integer[] copy = Arrays.copyOfRange(arr, 0, 2, Integer[].class);
System.out.println(Arrays.toString(copy));
```

- **정리**
1. `비검사 경고`는 중요하니 무시해서는 안된다.
2. 모든 비검사 경고는 `런타임`에 `ClassCastException` 가능성을 보여주는 것이므로 `제거`해야 한다.
3. 경고를 없앨 방법을 찾기 어렵다면, `타입 안전함`을 `증명`하고 범위를 좁혀 경고를 숨겨라.
