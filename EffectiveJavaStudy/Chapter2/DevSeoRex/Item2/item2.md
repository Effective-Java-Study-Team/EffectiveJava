### 아이템 2 - 생성자에 매개변수가 많다면 빌더를 고려하라

- **정적 팩토리 메서드와 생성자의 똑같은 제약 사항**
    - 선택적 매개변수가 많을때 적절히 대응하기 어렵다는 것
        - Computer라는 객체가 있다고 가정하면, 선택적인 매개변수 목록에 대응하기 어렵다.
        ![image](https://github.com/ch4570/file-block-extension/assets/91787050/987003d4-d85c-4596-a949-961a489b6f3c)
      - **점층적 생성자 패턴으로 해결할 수 있을까?**
      ![image](https://github.com/ch4570/file-block-extension/assets/91787050/8ca4d0af-d754-4820-8a16-58ee4a4cd239)

      점층적 생성자 패턴으로 해결하려 한다면, 선택적 매개변수를 몇개 선택하는지에 따라 생성자의 개수가 지속적으로 늘어가게 되고, **호출하는 클라이언트 입장에서도 어떤 생성자에 어떤 인자들이 제공되는지 알아보기가 점점 힘들어진다.**

      - **자바 빈즈 패턴으로 해결이 가능할까?**

        자바 빈즈 패턴이란 매개변수가 없는 생성자로 객체를 생성한 후 setter를 통해 값을 설정하는 방법이다.
        ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/3400c035-562f-4c2a-a9ab-28d0e6e74aa9)
        생성자를 활용한 방법보다는 가독성도 좋아지고 인스턴스도 만들기가 쉬워졌다.

        자바 빈즈 패턴의 단점은 객체 하나를 만들기 위해 **여러 개의 메서드를 호출**해야 하고, **객체가 완전히 생성되기 전까지는 일관성이 무너진 상태**에 놓이게 된다.

        점층적 생성자 패턴 에서는 최초 객체 생성시에만 값이 유효한지 확인해주면 되었는데, 자바 빈즈 패턴은 언제든지 **Setter를 활용해 값을 변경할 수 있다는 점**에서 완전히 그 장치가 무너져 버렸다.

        **무분별한 Setter 사용으로 인해서 언제 어디서 값이 변경되었는지 추적하기 어려운 것**도 큰 단점이 될 수 있고, 클래스를 불변으로 만들 수 없다는 단점도 존재한다.

      - **빌더 패턴을 이용해서 해결하자!**

        점층적 생성자 패턴의 안전성과 자바 빈즈 패턴의 가독성을 겸비한 빌더 패턴을 이용해 문제를 해결 할 수 있다.

        클라이언트는 필요한 객체를 직접 만드는 대신, 필수 매개변수 만으로 생성자(정적 팩터리 메서드)를 호출해 빌더 객체를 얻고, **원하는 선택 매개변수에만 값을 설정할 수 있다.**
        ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/4d959b49-7713-4629-bfd8-e38fa8792947)
        ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/25fcb642-9a74-4feb-9259-8f40a5a16567)

        ComputerFactory 클래스 내부에 FactoryBuilder 클래스를 만들어주었다.

        만약 **ComputerFactory를 생성**하고 싶다면 어떻게 코드가 호출될까?

        ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/aec052b3-dfd2-4019-9b04-3e000ed711f8)
        가독성과 안전성을 모두 builder 패턴을 활용해 보장받을 수 있게 되었다.

        또한 객체 내부에 잘못된 값을 넣으려는 시도를 한다면, **builder 내부에서 방어**할 수 있다.
        ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/2c7fb1c2-13ec-4b7d-a8e8-fcfdb6fb4e42)
        간단하게 값을 설정할때 IllegalArgumentExecption에 어떤 매개변수의 오류인지 작성해주었다.
        책에서는 **build 메서드를 호출할때 사용하는 생성자 내부에서 값들을 검사할 것을 권하고 있다.**

        - **빌더 패턴은 계층적으로 설계된 클래스와 함께 쓰기에 좋다.**
          ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/299e99bb-9e3d-4adc-b2df-dc86b0f66d90)

        SmartPhone 클래스를 상속받는 IPhone & SamsungPhone 클래스를 만들어서 계층적으로 설계된 클래스와 어떻게 쓰기 좋은지 확인해보자.
        ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/e2e9399d-5670-4275-ac94-bafc603dafc9)
        ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/45d2842c-d03c-418d-b5da-def04f7e7baf)

        중점적으로 살펴봐야 할 부분은 제네릭을 사용한 builder 부분과 self 메서드 부분이다.
        ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/4dd46dd7-fa5d-4c1f-be7c-74f248af63fa)

        self 메서드에서 반환하는 T를 보면, 클래스의 구현부의 **Builder<T extends Builder<T>>의 T를 의미한다.**

        self 메서드를 Builder 클래스를 상속받은 하위 Builder에서 오버라이딩 하기 때문에, self 메서드를 호출하면 각 Builder 타입에 맞는 인스턴스가 반환되게 된다.

        addOption은 부모 클래스의 메서드를 사용하는 것이기 때문에 **return this**를 하게되면 부모 타입의 builder가 반환되므로, 메서드 체이닝을 이용한 유연한 사용이 어려워진다.

        **따라서 공변 반환 타이핑을 이용해 클라이언트가 형변환에 신경 쓰지 않고도 빌더를 사용할 수 있게 한다.**


        

        



        
        
