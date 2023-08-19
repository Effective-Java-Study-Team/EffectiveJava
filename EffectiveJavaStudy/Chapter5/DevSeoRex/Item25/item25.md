### 아이템 25 - 톱레벨 클래스는 한 파일에 하나만 담으라

### 한 파일에 여러 톱레벨 클래스가 있을때의 문제
`소스 파일` 하나에 `톱레벨 클래스`를 여러 개 선언하더라도 `컴파일 에러`는 발생하지 않는다. <br>
하지만 아무런 득도 없고 `심각한 위험`을 감수해야 하는 문제가 있다.

한 클래스를 여러 가지로 정의할 수 있고, 어느 것을 사용할지는 `어떤 소스`를 `먼저 컴파일`하냐에 따라 달라지기 때문이다.

```java
public class Main {

    public static void main(String[] args) {
        System.out.println(Utensil.NAME + Dessert.NAME);
    }
}
```

```java
// Utensil.java

class Utensil {
    static final String NAME = "pan";
}

class Dessert {
    static final String NAME = "cake";
}
```

`Utensil` 과 `Dessert` 클래스가 `Utensil.java`라는 한 클래스에 정의되어 있다. <br>
이런 경우 `Main.java`를 컴파일 하면 `pancake`를 출력한다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/6fd48f33-bd98-4739-be8a-25ef5bacc8b9)

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/796d6d85-44a5-4e21-bb72-fa1ad9524f88)

```java
// Dessert.java

class Utensil {

    static final String NAME = "pot";
}

class Dessert {

    static final String NAME = "pie";
}
```

여기서 Dessert.java 파일에 `Utensil`과 `Dessert` 클래스를 중복 정의하면 문제가 발생한다. <br>
`javac Main.java Dessert.java` 명령으로 컴파일하면 책에서는 `컴파일 오류`가 난다고 되어 있지만 실행환경에서는 문제가 발생하지 않았다.

물론 컴파일 오류가 발생하지 않았다고 해서 문제가 없는 것은 아니였다.

- **Main.java 또는 Main.java Utensil.java 순서로 컴파일을 실행할경우**

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/ce63a40c-cc93-4d08-8f32-b67c93799143)

모두 `pancake`를 출력하는 것을 볼 수 있다.

- **Dessert.java Main.java 순서로 컴파일을 실행할경우**

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/033fe415-5b43-472d-baa3-ae0521b109ba)

`potpie`를 출력하고 있다.

이와 같이 어떤 `소스 파일`을 먼저 `컴파일`하는지에 따라서 출력 결과가 바뀌는 문제가 발생한다. <br>
하지만 요즘은 대부분 `IDE`를 사용하고 있기 때문에 이 코드를 작성하고 이미 `클래스가 중복 정의되었다는 경고 메시지`를 확인할 수 있었다.

### 이 문제를 해결하는 가장 쉬운 방법

이 문제를 해결하는 가장 쉬운 방법은 `톱레벨 클래스`들을 `서로 다른 소스 파일`로 `분리`하는 것이다. <br>
여러 `톱레벨 클래스`를 한 파일에 꼭 담아야 할 이유가 있다면 `정적 멤버 클래스`를 사용하는 것도 하나의 방법이 될 수 있다.

```java
public class Test {

    public static void main(String[] args) {
        System.out.println(Utensil.NAME + Dessert.NAME);
    }
    
    private static class Utensil {
        static final String NAME = "pan";
    }
    
    private static class Dessert {
        static final String NAME = "cake";
    }
}
```

- **정리**
1. `소스 파일` 하나에는 반드시 `톱레벨 클래스`를 `하나`만 담자.
2. 이 규칙만 따르면 `컴파일러`가 한 클래스에 대한 정의를 `여러 개` 만들어 내지 않는다.
