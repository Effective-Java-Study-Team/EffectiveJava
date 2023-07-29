### 아이템 5 - 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라

- **정적 유틸리티를 잘못 사용한 예 - 유연하지 않고 테스트하기 어렵다.**
    - 정적 필드 또는 정적 메서드만을 포함하고 있는 정적 유틸리티 클래스를 이용해 맞춤법 검사기를 위한 사전(dictionary)를 구현하는 것은 드물지 않게 볼 수 있다.
      ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/2706e256-a004-448a-8590-def7be0cff94)
    
    - **싱글턴을 잘못 사용한 예 - 유연하지 않고 테스트하기 어렵다.**
      - 싱글턴을 이용해서 맞춤법 검사기를 구현한다면 아래와 같이 코드를 작성할 수 있을 것이다.
        ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/b2ae6b6d-0a78-4f1d-8102-fad3c4396340)
        위에서 본 두가지 방식은 그다지 좋은 방법이라고 할 수 없다.<br>
        현재 Lexicon(사전) 객체는 하나이다. 즉 다루는 사전 객체가 하나이기 때문에 이 코드는 **현재 문제가 없다.**

        맞춤법 사전은 언어별로 따로 있고, 특수 어휘용 사전을 별도로 두기도 하는 등 매우 복잡한 로직을 수행해야한다.<br>
        만약 **국어 사전, 영어 사전, 프랑스어 사전** 세가지 사전을 이용해 맞춤법 검사기를 구현해야 한다면 어떻게 해야할까?

        가장 쉬운 방법은 **final 한정자를 제거**하고, **다른 사전으로 교체하는 메서드를 추가**하는 방법일 것이다.
        ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/e3d28772-7cf9-4226-a64a-5338e8bf392e)

        이 방식은 어색하고 오류를 내기 쉬우며, 멀티스레드 환경에서는 쓸 수 없다.

        정적 유틸리티 클래스 방식이나 싱글턴으로 멀티 스레드 환경에 대응하기 어려운 이유는,<br>
        여러 스레드가 한 자원을 사용하기 위한 **경합 상태(Race Condition)가 발생할 가능성이 큰 점**과<br>
        정적 변수를 활용에 단일 인스턴스에 접근하여 사전(Lexicon)의 인스턴스를 계속 교체할 경우 **예상하지 못한**<br>
        **동작이 발생할 수 있는 문제**가 있다.

        여러 스레드가 공유되고 있는 자원을 동시에 교체한다면 원하지 않는 객체가 동작하거나 **동기화 문제를 만들 수 있다는 문제**도 있다.

        이 세가지 문제를 간단하게 해결 해 줄 수 있는 방법은 의존 객체 주입을 활용한 방법이다.<br>
        흔히 **DI(Dependency Injection)** 이라 부른다.

        인스턴스를 생성할 때 생성자에 필요한 자원을 넘겨주는 방식으로 주입할 수 있다.<br>
        **국어 사전, 영어 사전, 프랑스어 사전**이 있을때 의존 객체 주입을 활용한 방법의 예시 코드를 짜보자.

        ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/85e83417-200f-409d-ac85-33dfc3a9e753)
        ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/00f6f8c3-1b3c-49e4-a92f-a35d32bd3a1b)
        ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/11058299-abdd-4f9c-ac6f-911b5861959a)
        ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/75f1e09a-8599-4c2b-b684-341d3f220c53)

        **Lexicon 클래스**를 인터페이스로 변경하고, **SpellChecker는 인터페이스에 의존하도록 변경**한다.
        ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/85c5d8bc-6961-4ec4-b855-7eb69e3be21a)
        ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/728fe7aa-8284-4d6d-b8d7-c5730b1c279d)
        ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/421636d2-1d66-4ad3-8c01-9959c2a150b4)

        사용하는 자원(사전)에 따라 동작이 달라지는 것을 볼 수 있다.

        생성자로 사전을 주입하는 코드를 **Supplier를 활용한 코드로 변경하면, 메서드 레퍼런스를 넘길 수 있다는 장점**도 있고, <br>
        각 사전들의 **인스턴스를 하나만 유지하며 재사용할 수 있다.**
    
        ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/e8c02261-a7d5-4e34-831d-0c1674a8ad1e)


        각 사전 클래스는 static final 필드로 유일한 인스턴스를 가지고 있고, **getXXXDictionary 라는 명명규칙으로** <br>
        **인스턴스를 얻어오는 정적 팩터리 메서드**를 추가하면 아래와 같이 코드를 수정할 수 있다.

        지금은 각 사전별로 한번씩 인스턴스를 호출해 사용했지만, 만약 이 사전의 객체를 여기저기서 스레드별로 생성한다면 **객체의 개수가 어마어마 하게 증가**할 것이다.
        사전은 각 **사전별로 정형화된 동작이 정의**되어 있고, 맞춤법 검사기와 같이 사용하는 자원에 따라 동작이 바뀌지 않기 때문에 싱글톤 또는 정적 팩토리 메서드를 활용한 객체 재사용이 좋은 방법같다.

        의존 객체 주입은 의존성이 수천 개나 되는 큰 프로젝트에서는 코드를 어지럽게 만드는 원인이 될 수 있다.<br>
        이런 문제를 해결하기 위해, **Spring Framework 와 같은 의존 객체 주입 프레임워크를 사용**해서 이런 어질러짐을 해소할 수 있다.




        







