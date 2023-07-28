### 아이템 3 - private 생성자나 열거 타입으로 싱글턴팀을 보증하라

- **싱글턴을 만드는 방식**
    - private 생성자와 public static 멤버를 이용하기
 
      ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/a4c3a6fa-6763-4e2f-95f7-7098c394a56e)

      인스턴스를 외부에서 생성하지 못하도록 **생성자를 private 제어자**로 외부 노출이 되지 않게 하고,

      public static 멤버로 **Elvis의 유일한 인스턴스를 두어서 싱글턴임을 보장**할 수 있는 방법이다.

      이 방법은 권한이 있는 클라이언트가 Reflection API를 이용해 private 생성자를 호출하면 객체를 생성할 수 있다는 문제가있다.

      ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/5745d290-d57b-47c3-84a5-164e2960241d)

      이를 방어하기 위해서는 싱글턴 객체가 생성될 때 단 한번을 제외하고는 **생성자 호출시 예외를 발생**시켜서 <br>
      두번째 객체 생성을 막을 수 있다.

      ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/bd9a30fa-2798-489c-b669-37a5a41143a6)

      - 정적 팩토리 메서드를 이용한 싱글턴

      필드에 접근하는 대신 getInstance 메서드를 정의해서 유일한 인스턴스를 반환하는 방법이다.

      ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/b9d7cef2-50bb-4ee0-a993-fb2d677de8de)

      정적 팩토리 메서드를 도입하지 않고, 필드에 직접 접근해서 반환 받는 것도 나쁜 방법은 아니다.<br>
      하지만 정적 팩토리 메서드를 도입했을때 얻게 되는 장점이 꽤 많다.

      첫 번째 장점은 API를 바꾸지 않고도 싱글턴이 아니게 변경할 수 있다는 점이다.<br>
      정적 팩터리 메서드가 아닌경우, private 생성자 제약을 풀어야만 새로운 객체를 만들 수 있었다.<br>
      사용하는 메서드 시그니처가 달라지게 되어 API의 변경이 오게 되는 것이다.

      정적 팩터리 메서드를 지금과 같이 사용한다면 인스턴스를 반환하는 부분을 항상 새로운 객체를 반환하도록 약간의 코드 수정만 하면 된다.<br>
      즉, 클라이언트는 메서드 시그니처의 변경이 없으므로 기존의 코드를 계속 사용할 수 있기 때문에<br>
      **API의 변경 없이도 유연하게 대처할 수 있다.**

      ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/bf5aacef-7d17-46ff-bc35-ba86aff38f25)

      두 번째 장점은 정적 팩터리의 메서드 참조를 공급자(Supplier)로 사용할 수 있다는 점이다.
      ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/bb18077a-4a2e-4b7c-8a01-13ec17d54e8a)
      예를 들어 Supplier<ElvisWithSupplier> 를 매개변수로 받는 메서드가 있다고 가정하면, <br>
      클라이언트에서 코드를 호출할때 메서드 레퍼런스를 넘기는 방식으로 유연하게 대처할 수 있다는 점이다.

      ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/372f84e4-d66e-4b16-9741-3b0c2ae8e5aa)

    - **Enum을 활용한 싱글턴 보장**
      - 열거형은 인스턴스화가 불가능하므로 싱글턴임을 보장받을 수 있다.
      - Enum 이외의 다른 클래스를 상속 받을 수 없고, 인터페이스는 구현할 수 있다.
      - 상속을 받아야 하는 클래스의 경우 열거형으로 싱글턴임을 보장받을 수 없다는 특징이있다.

     ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/b08dde81-aa74-48cd-8cd9-2d2ebe4fd256)

     열거형 타입 클래스에 public 생성자를 만들려고 시도하면 컴파일 타임에 에러가 발생해서 여러 인스턴스를 만들 방법이 막혀있다.

     ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/0a4eb6c7-ba17-464f-887a-8176ddfb416c)
     ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/e7ca8be7-e0f6-441d-bcfb-ae5f2ddb0092)

     Reflection API를 사용해도 인스턴스화 하지 못하도록 원천봉쇄 되어 있기 때문에 싱글턴임을 늘 보장받을 수 있다.

    - **직렬화로 인해 생기는 문제**
      - 싱글턴 클래스를 직렬화하려면 단순히 Serializable을 구현한다고 해서 보장되지 않는다.
      - readResolve 메서드를 제공해야만 싱글턴을 보장할 수 있다.

     readResolve 메서드를 정의하지 않은 클래스로 직렬화를 진행했을때<br>
     **직렬화 전 객체와 직렬화를 거친 바이트 배열을 역직렬화 한 객체가 다른 인스턴스인지 테스트** 하기 해보면 결과가 아래와 같다.

    ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/c2d9906b-4e94-428e-ba00-28d4c88a7b85)
    먼저 객체를 직렬화하고 역직렬화 해주는 유틸성 클래스를 만들었다.

    ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/84d97433-26cb-4c45-a67e-59ad16cec794)
    이제 MyInt 클래스의 인스턴스를 직렬화 → 역직렬화 하여 기존 객체와 비교하면,

    ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/142fdd8f-fcd9-479d-8337-a3126867e4c6)
    동일한 객체가 아니라고 나온다. 왜 그럴까?

    직렬화 후 역직렬화를 할때 항상 새로운 객체를 만들어 반환하기 때문이다.<br>
    transient 키워드를 필드에 붙이는 것은 직렬화를 할때 필요하지 않은 일시적인 정보라는 마킹을 해두는 것이므로,<br>
    **MyInt의 값이 0으로 바뀌어서 새로운 객체를 반한하는 것 외에는 달라지는 것은 없다.**

    ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/08ac5ee9-b474-4603-b843-26e89096d2b7)
    ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/ba629ac3-90b0-4a0c-a4c5-0d77d5b6e94b)

    그럼에도 transient 키워드를 붙이는 이유는, 싱글턴을 보장하여 이미 만들어진 객체를 재사용 할것이기 때문에 **number의 정보가 직렬화시 필요하지 않아서 transient를 붙여 포함하지 않도록 하기 위함이다.**

    readResolve 메서드를 정의해두면 직렬화 시에도 싱글턴임을 보장받을 수 있다.<br>
    그 비밀은 ObjectInputStream의 구현을 보면 알 수 있다.

    내부적으로 readResolve 메서드가 있는지 확인 후 있다면 사용하게 되고 없다면 기본 역직렬화 방법을 택하기 때문이다.
    ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/2f48d986-4922-4f5b-afbe-18523b4b7c68)

    readResolve 메서드를 정의해두면 싱글턴임을 보장받을 수 있게 된다.
    ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/2e157919-2402-4fb3-ab38-d97bcffb2518)






     


