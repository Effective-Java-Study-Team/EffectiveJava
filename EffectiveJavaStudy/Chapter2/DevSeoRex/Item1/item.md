### 아이템 1 - 생성자 대신 정적 팩터리 메서드를 고려하라

- 정적 팩토리 메서드는 디자인 패턴에서의 팩토리 메서드와 다르다.
    - 디자인 패턴 중에는 이와 일치하는 패턴은 없다.
    - 디자인 패턴에서의 팩토리 패턴이란?
        - 객체를 사용하는 코드에서 객체 생성 부분을 떼어내(팩토리 클래스로 분리) 추상화한 패턴이자, 상속 관계에 있는 두 클래스에서 상위 클래스가 중요한 뼈대를 결정하고, 하위 클래스에서 객체 생성에 관한 구체적인 내용을 결정하는 패턴이다.

- 정적 팩터리 메서드의 장점
    - **이름을 가질 수 있다.**
        - 생성자에 넘기는 매개변수와 생성자 자체만으로는 반환될 객체의 특성을 설명하지 못한다.
        - 정적 팩터리 메서드는 이름만 잘 지으면 반환될 객체의 특성을 쉽게 묘사할 수 있다.
        - 한 클래스에 시그니처가 같은 생성자가 여러개 필요할 것 같다면 정적 팩터리 메서드로 변경하는 것을 고려하자
    - **호출될 때 마다 인스턴스를 새로 생성하지는 않아도 된다.**
        - 인스턴스를 미리 만들어 놓거나 새로 생성한 인스턴스를 캐싱하여 재활용하는 식으로 불필요한 객체 생성을 막는다.
        - 플라이 웨이트 패턴도 이와 비슷한 기법이라 할 수 있다
            - 플라이 웨이트 패턴이란?
                - 자주 변하는 속성과 변하지 않는 속성을 분리하고 재사용하여 메모리 사용을 줄이는 패턴
                - 플라이 웨이트 패턴은 클래스를 팩토리에서 제어한다.
                - Java의 Integer.valueOf 메서드에서 활용하는 예시를 찾아볼 수 있다.
                    - Integer 클래스는 내부적으로 -127 ~ 128 범위에 드는 값을 캐싱한다.
                
                ![image](https://github.com/ch4570/file-block-extension/assets/91787050/945d54f6-5771-445c-9f5f-2a3ad5332db3)
                
        - 반복되는 요청에 같은 객체를 반환하는 방법으로 인스턴스를 살아 있게 할지를 철저히 통제할 수 있다
            - 인스턴스를 통제하면 싱글턴이나 인스턴스화 불가한 클래스로 만들 수 있다.
            - 동치인 인스턴스가 단 하나뿐임을 보장할 수도 있다.
    - **반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있다.**
        - 구현 클래스를 공개하지 않고도 그 객체를 반환할 수 있어, API를 작게 유지할 수 있다.
        - 인터페이스를 정적 팩터리 메서드의 반환 타입으로 사용하는 인터페이스 기반 프레임워크를 만드는 핵심 기술이기도 하다.
        - 컬렉션 프레임워크는 45개의 유틸리티 구현체 대부분을 정적 팩터리 메서드를 통해 얻도록 했다.
        - Java 8 부터 인터페이스에 정적 메서드를 선언할 수 있게 되었기 때문에, 동반 클래스를 만들어 정의할 필요가 없어졌다.
            
            ```java
            // Stream 인터페이스는 인터페이스 내부의 정적 팩토리 메서드를 사용 -> Since JDK 1.8 ~
            Stream<String> stream = Stream.of("name");
                    
            // Collection 인터페이스는 인스턴스화 불가한 Collections 동반객체를 이용해 팩토리 메서드를 사용
            Collection<String> collection = Collections.emptyList();
            ```
            
    - **입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다.**
        - 반환 타입의 하위 타입이기만 하면 어떤 클래스의 객체를 반환하든 상관없다.
            
            
          ![image](https://github.com/ch4570/file-block-extension/assets/91787050/4b055ec3-5ee8-4283-a51f-02780b78e86a)
            
        - 클라이언트는 팩터리가 건네주는 객체가 어느 클래스의 인스턴스인지 알 수도 없고 알 필요도 없다.
    - **정적 팩터리 메서드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다.**
        - 서비스 제공자 프레임워크는 3개의 핵심 컴포넌트로 이루어진다.
            - 구현체의 동작을 정의하는 서비스 인터페이스(**Connection**)
            - 제공자가 구현체를 등록할때 쓰는 제공자 등록 API(**DriverManager.registerDriver**)
            - 클라이언트가 서비스의 인스턴스를 얻을 때 사용하는 서비스 접근
                - API(**DriverManager.getConnection**)
            - (옵션 → 필수는 아님) 팩토리 객체를 설명해주는 서비스 제공자 인터페이스(**Driver**)
        - JDBC API를 간략하게 직접 코드로 구현해보면 이해할 수 있다.
      ```java
      public interface Connection {
    
      }

      public interface Driver {

      Connection getConnection(String url, String username, String password);

      }
      ```
      **Connection & Driver** 인터페이스는 구현 클래스 없이 인터페이스만 정의된 상태이다.
      ```java
      public class DriverManager {

        private final static CopyOnWriteArrayList<Driver> drivers = new CopyOnWriteArrayList<>();

        private DriverManager() {}


        public static Connection getConnection(String url, String username, String password) {

          for (Driver driver : drivers) {
              Connection connection = driver.getConnection(url, username, password);

              if (connection != null) {
                  return connection;
              }
          }

            return null;
        }

        public static void registerDriver(Driver driver) {
            drivers.add(driver);
        }
      }
      ```
      Connection을 반환하고 Driver를 등록하는 DriverManager는 Connection을 얻을 수 있는 

    **정적 팩토리 메서드**를 제공한다.

    여기서 유심히 살펴봐야 할 내용은 **Driver와 Connection의 구현 클래스가 존재하는지 여부와 상관 없이** 정적 팩토리 메서드의 코드 수정 없이, 구현 클래스를 얼마든지 추가할 수 있다는 점이다.

    만약 여기서 MyDB라는 벤더사의 MyDriver 클래스를 추가한다고 하자.

    ```java
    public class MyDriver implements Driver {

    static {
       DriverManager.registerDriver(new MyDriver());
       System.out.println("드라이버 등록!");
    }

    @Override
    public Connection getConnection(String url, String username, String password) {
       if (url.equals("MyDriver")) {
           return new ConnectionImpl();
       }
        
       return null;
      }
    }
    ```

    MyDriver 클래스를 로딩하고, url 패턴이 MyDriver와 일치할때만 Connection을 반환하도록 코드를 작성한 뒤 테스트 하면 아래와 같다.

    ```java
    public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.adoptpet.server.adopt.controller.MyDriver");
        Connection connection = DriverManager.getConnection("", "", "");
        System.out.println(connection);
        System.out.println();

        Connection connection2 = DriverManager.getConnection("MyDriver", "", "");
        System.out.println(connection2);
      }
    }
    ```

    ![image](https://github.com/ch4570/file-block-extension/assets/91787050/3dd5463d-8bea-41d8-8cb8-7f50268bb19d)
    만약 다른 벤더사의 드라이버를 추가한다고 해도 결과에는 변함이 없다.
    ```java
   public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
          Class.forName("com.adoptpet.server.adopt.controller.MyDriver");
          Connection connection = DriverManager.getConnection("YourDriver", "", "");
          System.out.println(connection);
          System.out.println();
  
          Connection connection2 = DriverManager.getConnection("MyDriver", "", "");
          System.out.println(connection2);
          System.out.println();
  
          Class.forName("com.adoptpet.server.adopt.controller.YourDriver");
          Connection connection3 = DriverManager.getConnection("YourDriver", "", "");
          System.out.println(connection3);
      }
    }
    ```
    ![image](https://github.com/ch4570/file-block-extension/assets/91787050/3ffb138c-59b3-41df-b7b3-52f5f8c00f75)

    JDK 6 이후로는 ServiceLoader를 활용하기 때문에 서비스 제공자 프레임워크를 직접 만들지 않아도 된다. 따라서 JDK 6 이후의 DriverManager의 구현부를 보면 ServiceLoader를 사용하는 것을 볼 수 있다.

    따라서 JDK 6 이후로는 JDBC 활용을 위해 Class.forName을 이용한 클래스 로딩이 필요하지 않다.
    ![image](https://github.com/ch4570/file-block-extension/assets/91787050/c4504972-4c8d-45ca-a02d-e7c025f72532)

    - **정적 팩토리 메서드의 단점**
    - 정적 팩토리 메서드만 사용하면 하위 클래스를 만들 수 없다.
        - 상속보다는 컴포지션(합성)을 사용하도록 유도하고 불변 타입을 만들려면 이 제약을 지켜야 한다는 점에서 오히려 장점이라고 볼 수도 있다.
    - 정적 팩토리 메서드는 프로그래머가 찾기 어렵다.
        - 생성자처럼 API 설명에 명확히 드러나지 않으니 사용자가 방법을 알아내야 한다.
        - 메서드 이름을 널리 알려진 규약을 따라 대부분 짓고 있으니 큰 문제가 없기에 이것또한 단점이라고 보기 어렵다.
    
