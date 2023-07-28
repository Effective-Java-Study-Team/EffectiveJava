### 아이템 4 - 인스턴스화를 막으려거든 private 생성자를 사용하라

- 정적 멤버만 담은 유틸리티 클래스는 인스턴스로 만들어 쓰려고 설계한 것이 아니다.
- 생성자를 명시하지 않으면 컴파일러는 public 생성자를 자동으로 만들어 주기 때문에 명시적으로 인스턴스화를 막아주어야 한다.
- 공개된 API 들에서도 의도치 않게 인스턴스화할 수 있게 된 클래스가 종종 목격되곤 한다.

- 추상 클래스로 만드는 것으로는 인스턴스화를 막을 수 없다.
    - 하위 클래스를 만들어 인스턴스화 하면 그만이기 때문에 사용자는 상속해서 쓰라는 뜻으로 오해할 수 있다.
    - private 생성자를 추가해서 인스턴스화를 막는 것이 가장 명시적이고 좋은 방법이다.

- private 생성자를 만든 뒤에는 설명이 필요하다.
    - 생성자는 분명 존재하는데 호출할 수 없게 되어있다는 것은 그다지 직관적이지 못하다.
    - 따라서 간단한 주석을 달아서 적절한 설명을 해주도록 하자.
      ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/52c72f99-65db-4c94-a750-f45f5df57064)

- 이 방식은 상속을 불가능하게 하는 효과도 있기 때문에 상속보다 Composition 유도할 수 있다.
    - 모든 생성자는 명시적이든 묵시적이든 상위 클래스의 생성자를 호출하게 되는데 이를 private로 선언했으니 하위 클래스가 상위 클래스의 생성자에 접근할 길이 막힌다.
      ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/87a4e7e0-d2f4-4b22-a837-9cf6a9045e3c)
