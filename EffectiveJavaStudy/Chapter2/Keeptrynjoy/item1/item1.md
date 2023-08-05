# ITEM 1. "생성자 대신 정적 팩토리 메서드를 고려하라."

## 정적 팩토리 메서드

### 장점

- 이름을 가지는 생성자를 만들 수 있음
    - 하나의 시그니처로 생성자를 하나만 만들 수 있다. 한 클래스에 시그니처가 같은 생성자가 여러 개 필요할 것 같으면, 생성자를 정적 팩토리 메서드로 바꾸고 각각 차이를 잘 드러내는 이름으로 작성 가능.


- 호출시 인스턴스를 새로 생성하지 않아도 됨
    - 불필요한 객체 생성을 피할 수 있음.
        - `Boolean.valueOf(boolean)`
        - 같은 객체가 자주 요청되는 상황이라면 성능을 상당히 올려줌
        - **플라이웨이트 패턴(Flyweight pattern)** 과 비슷한 기법
            - Flyweight pattern 이란?
              - 어떤 클래스의 인스턴스 한 개만으로 여러 개의 ‘가상 인스턴스’를 제공하고 싶을 경우 사용하는 디자인 패턴. 즉, 인스턴스를 가능한한 공유하여 new 연산자를 통한 메모리 낭비를 줄이는 방식. 
              '공유할 객체', '객체의 인스턴스를 생성하고 공유해주는 팩토리', '패턴을 사용할 고객, 클라이언트' 
              이 세가지 컴포넌트로 구현 가능.
              - 위와 같은 특징으로 Java의 String constant pool에 적용되었음. String이 만들어질때 pool에 동일한 문자열이 있다면 이를 불러오는 방식으로 구현하여 메모리를 절약. 

              <br/>
    - 인스턴스 통제(instance-controlled) 클래스
        - 반복되는 요청에 같은 객체를 반환하는식의 정적 팩토리 방식 클래스
        - 언제 어느 인스턴스를 살아 있게 할지를 철저히 통제.
        - 인스턴스 통제시 클래스를 싱글톤으로 만들 수도, 인스턴스화 불가로 만들 수도 있음.
        - 동치인 인스턴스가 단 하나 뿐임을 보장할 수 도 있음<br/>( a == b 일때만 a.equals(b)가 성립)


- 반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있음.
    - 반환할 객체의 클래스를 자유롭게 선택 가능
    - **인터페이스 기반 프레임워크를 만드는 핵심기술**
    - 자바 컬렉션 프레임워크는 핵심 인터페이스들에 수정 불가나 동기화 등의 기능을 덧붙인 총 45개의 유틸리티 구현체를 제공. 이 구현체 대부분을 단 하나의 인스턴스화 불가 클래스인 java.util.Collections에서 정적 펙토리 메서드를 통해 얻도록함.
    - 정적 펙토리 메서드를 사용하는 클라이언트는 얻은 객체를 인터페이스만으로 다루게 됨


- 입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있음.
    - 클라이언트는 팩토리가 건네주는 객체가 어느 클래스의 인스턴스인지 알 수 없고 알 필요도 없음.


- 정적 펙터리 메서드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 됨
    - 이런 유연함은 서비스 제공자 프레임워크를 만드는 근간이 되며,
      대표적인 서비스 제공자 프레임 워크로는 JDBC(Java Database Connectivity)가 있음.
        - 제공자(provider)는 서비스의 구현체.
        - 구현체들을 클라이언트에 제공하는 역할을 프레임워크가 통제.
        - 클라이언트를 구현체로 부터 분리시켜줌
      
        <br/>
    - 서비스 제공자 프레임워크의 핵심 컴포넌트 3가지
        1. 구현체의 동작을 정의하는 서비스 인터페이스
        2. 제공자가 구현체를 등록할 때 사용하는 제공자 등록 API
        3. 클라이언트가 서비스의 인스턴스를 얻을 때 사용하는 서비스 접근 API.

           → **이 서비스 접근 API가 바로 서비스제공자 프레임 워크의 근간인 ‘유연한 정적 펙토리’의 실체**
        
            <br/>
        - 종종 서비스 제공자 인터페이스라는 4번째 컴포넌트가 쓰이기도함.
          <br/>(인스턴스를 생성하는 팩토리객체를 설명해줌)
        
        - 예시
            ```java
            // 서비스 인터페이스
            public interface Connection 
            
            public class DriverManager{
            	// 제공자 등록 API 
            	public static void deregisterDriver(Driver driver) 
            	
            	// 서비스 접근 API
            	@CallerSensitive
                    public static Connection getConnection(String url,
                                            java.util.Properties info)
            
            }
            ```


<br/>

### 단점

- 상속을 하려면 public이나 protected 생성자가 필요하니 정적 팩토리 메서드만 제공하면 하위 클래스를 만들 수 없음.
    - 컬렉션 프레임워크의 유틸리티 구현 클래스들은 상속할 수 없다는 이야기. 이 제약은 상속보다는 **컴포지션**을 사용하도록 유도하고 불변타입으로 만들려면 이제약을 지켜야한다는 점에서 강점이 될 수 있음
        
        <br/>
        
        - 컴포지션 방식 예시
            ```java
            // 1. 상속을 이용한 자동차 구현
            class Engine {
                void start() {
                    System.out.println("Engine starting...");
                }
            }
            
            class Car extends Engine {
                void drive() {
                    start();
                    System.out.println("Car is driving.");
                }
            }
            
            public class Main {
                public static void main(String[] args) {
                    Car car = new Car();
                    car.drive();
                }
            }
            ```

          위의 코드에서 **`Car`** 클래스는 **`Engine`** 클래스를 상속받고 있습니다. **`Car`** 클래스에서 **`start`** 메서드를 호출하고 있음. 이 코드는 동작은 잘 하지만 상속을 사용하여 **`Engine`** 의 기능을 확장한 것이므로, **`Car`** 클래스와 **`Engine`** 클래스 간에 강한 결합이 생기게 됨.

            ```java
            // 2. 컴포지션을 이용한 자동차 구현
            class Engine {
                void start() {
                    System.out.println("Engine starting...");
                }
            }
            
            class Car {
                private Engine engine;
            
                public Car() {
                    engine = new Engine();
                }
            
                void drive() {
                    engine.start();
                    System.out.println("Car is driving.");
                }
            }
            
            public class Main {
                public static void main(String[] args) {
                    Car car = new Car();
                    car.drive();
                }
            }
            ```

          위의 코드에서 **`Car`** 클래스는 **`Engine`** 클래스를 포함하고 있습니다. **`Car`** 클래스는 **`Engine`** 객체를 생성하여 이를 활용합니다. 이제 **`Car`** 클래스와 **`Engine`** 클래스는 더 이상 상속 관계가 아님. 
          
          이렇게 하면 컴포지션을 사용하여 두 클래스 간에 느슨한 결합을 형성하게 됨. **`Car`** 클래스는 **`Engine`** 의 기능을 활용할 수 있지만, **`Engine`** 클래스의 내부 구현을 자세히 알 필요가 없습니다. 또한, 미래에 **`Engine`** 클래스의 구현이 변경되어도 **`Car`** 클래스에는 영향을 주지 않음. 컴포지션은 상속보다 객체 간의 결합도를 낮추고, 유연하고 확장 가능한 코드를 작성할 수 있도록 도와줌.
      
    <br/>
- 정적 팩토리 메서드는 프로그래머가 찾기 어려움.
    - 사용자가 정적 팩토리 메서드 방식 클래스를 인스턴스화할 방법을 알아내야 함.
    - 정적팩토리 메서드에 흔이 사용하는 명명 방식
        - from, of, valueOf, instance 또는 getInstance, create 또는 newInstance, get’Type’, new’Type’, ‘type’