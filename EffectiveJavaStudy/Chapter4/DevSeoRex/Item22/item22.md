### 아이템 22 - 인터페이스는 타입을 정의하는 용도로만 사용하라

### 인터페이스의 목적

`인터페이스`는 자신을 구현한 클래스의 `인스턴스`를 참조할 수 있는 `타입 역할`을 한다. <br>
`인터페이스`는 클래스가 인터페이스를 구현하면, 자신의 인스턴스로 무엇을 할 수 있는지 클라이언트에 알려주는 역할을 한다.

`인터페이스`는 오직 `이 용도로만 사용`해야 하는것이다.

### 목적을 어긴 인터페이스 - 상수 인터페이스

`상수 인터페이스`란 메서드 없이 `static final 상수 필드`만 가득 찬 인터페이스를 말한다.

```java
public interface PhysiclConstants {
    
    static final double AVOGADROS_NUMBER = 6.022_140_857e23;

    static final double BOLTZMANN_CONSTANT = 1.380_648_52-23;

    static final double ELECTRON_MASS = 9.109_383_56e-31;
}
```

`상수 인터페이스`는 인터페이스를 잘못 사용한 대표적인 예시이며 `안티패턴`이다. <br>
`클래스 내부에서 사용하는 상수`는 외부 인터페이스가 아니고 `내부 구현`에 해당한다.

`상수 인터페이스` 를 구현하는 행위는 내부 구현을 `public API` 로 노출하는 행위다.

`클래스` 가 `상수 인터페이스` 를 사용하는 것은 사용자에게 아무런 의미를 주지 않는다. <br>
하지만 클라이언트가 이 `상수 인터페이스` 를 사용하게 된다면 다음 릴리스에 이 상수 인터페이스가 필요없는 방향으로 `내부 구현을 변경` 하려 해도 이미 때는 늦어버린 것이다.

`final 클래스`가 아닌 클래스가 `상수 인터페이스를 구현`한다면 모든 하위 클래스의 `이름공간`이 상수들로 오염되게 된다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/5a3051cd-cea7-42c2-a159-1706b38fd80a)

`자바 플랫폼에`도 상수 인터페이스가 몇 개 존재한다. <br>
인터페이스를 잘못 활용한 `대표적인 예시`이기 때문에 절대 따라해서는 안된다.

### 상수를 공개하려면 어떻게 해야할까

상수 인터페이스로 `상수를 공개하는 것`은 좋지 못한 방법이라고 했다.
그렇다면 상수를 공개할 목적이 있다면 어떻게 하는 것이 좋을까? <br>

- **클래스와 인터페이스에 강하게 연관된 상수라면 클래스나 인터페이스에 추가**

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/a0a7322c-34e6-40a6-92fe-ab6ac929e040)

대표적으로 `Integer`와 `Double`에 선언된 상수들이 이런 예시에 부합한다.

- **열거 타입으로 나타내기 적합한 상수라면 열거 타입으로 만들어 공개한다.**

```java
public enum EnumConstant {

    AVOGADROS_NUMBER(6.022_140_857e23),
    BOLTZMANN_CONSTANT(1.380_648_52-23),
    ELECTRON_MASS(9.109_383_56e-31);
    
    private final double number;

    EnumConstant(double number) {
        this.number = number;
    }
}
```

열거 타입으로 상수를 제공하는 것도 하나의 방법이 될 수 있다. <br>
`열거 타입`은 `타입 안정성`을 제공하는 등 많은 장점을 가지고 있다.

- **인스턴스화 할 수 없는 유틸리티 클래스에 담아 공개한다.**

```java
public class ConstantUtil {
    
    private ConstantUtil() {}

    static final double AVOGADROS_NUMBER = 6.022_140_857e23;

    static final double BOLTZMANN_CONSTANT = 1.380_648_52-23;

    static final double ELECTRON_MASS = 9.109_383_56e-31;
}
```

`private 기본 생성자`를 가지고, `상수 값`을 가지는 클래스를 만들어 `유틸성 클래스`로 제공할 수 있다.

- **정리**
 1. 인터페이스는 타입을 정의하는 용도로만 사용해야 한다.
 2. 상수 공개용 수단으로 사용하지 말자.
