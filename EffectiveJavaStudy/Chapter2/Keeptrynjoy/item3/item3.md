# ITEM 3. "private 생성자나 열거 타입으로 싱글턴임을 보증하라."

## 싱글턴(singleton)이란?
- 인스턴스를 오직 하나만 생성할 수 있는 클래스를 뜻 한다.
- 무상태(stateless) 객체나 시스템 설계상 유일해야하는 시스템 컴포넌트를 예로 들 수 있다. <br/>
  (데이터베이스 연결, 네트워크 연결 등)


## 기존 싱글턴을 만드는 두 가지 방식
### 1. public static final 필드 방식의 싱글턴
```java
public class Iphone {
    public static final Iphone INSTANCE = new Iphone();

    private Iphone(){}

    public void on(){
        System.out.println("The screen is turning on.");
    }
}

public static void main(String[] args){
    Iphone iphone = Iphone.INSTANCE;
    iphone.on();
}
```
- public이나 protected 생성자가 없음으로 Iphone 클래스가 초기화될 때 만들어진 인스턴스가 전체 시스템에 하나뿐임을 보장한다.

<br/>

### 2. 정적 팩토리 방식의 싱글턴
```java
public class Apple {
    private static final Apple INSTANCE = new Apple();

    private Apple(){}
    //Apple.getInstance()가 항상 같은 객체의 참조를 반환하므로 제 2의 인스턴스는 만들어지지 않는다.
    public static Apple getInstance(){
        return INSTANCE;
    }

    public void sellPhone(){
        System.out.println("your money, my money");
    }
}
```
  - API를 바꾸지 않고도 싱글턴이 아니게 변경 가능하다.
  
  - 정적 팩토리의 메서드 참조를 공급자로 사용할 수 있다.
    ```java
    public static void main(String[] args){
       Supplier<Apple> appleSupplier = Apple::getInstance;
       Apple apple = appleSupplier.get();
    }
    ```
  - 정적 팩토리를 제네릭 싱글턴 팩토리로 만들 수도 있다.
    - 제네릭은 컴파일 단계가 아닌 런타임 단계에서 타입 정보가 **소거**된다. 이런 특징을 활용하여 하나의 객체를 어떤 타입으로든 매개 변수화할 수 있다.
      > 소거란? 
    <br/> 원소 타입을 컴파일 타입에만 검사하고 런타임에는 해당 타입 정보를 알 수 없다는 것이다. 즉, 컴파일시에만 타입 제약 조건을 정의하고, 런타임에는 타입을 제거한다는 뜻이다.

        ```java
        import java.util.function.UnaryOperator;
      
        public class GenericSingletonFactory {
          
           private static UnaryOperator<Object> IDENTITY_FN = (t) -> t;
      
           @SuppressWarnings("unchecked")
           public static <T> UnaryOperator<T> identityFunction() {
               return (UnaryOperator<T>) IDENTITY_FN;
           }
        }
        ```

        - 위 코드는 항등함수를 담은 클래스이다. 소거방식을 사용할 경우 제네릭 싱글턴 하나면 충분하다.
        - T가 어떤 타입이든 UnaryOperator가 UnaryOperator가 아니기 때문에 경고가 발생한다.
        - 항등함수는 입력값을 그대로 반환 하기 때문에 개발자가 타입이 안전하다는 것을 인지가능하다. 그렇기에 SuppressWarinings 어노테이션을 추가해 경고를 지워줌으로써 깔끔하게 컴파일 할 수 있다.
    
    <br/>
    
    - 제네릭 싱글턴을 사용한 클라이언트 코드
      ```java
      public static void main(String[] args) {
          String[] strings = { "KIM", "LEE", "SEO" };
          UnaryOperator<String> sameString = identityFunction();
              
          for (String s : strings)
          System.out.println(sameString.apply(s));
 
                   
          Number[] numbers = { 1, 2.0, 3L };
          UnaryOperator<Number> sameNumber = identityFunction();
              
          for (Number n : numbers)
          System.out.println(sameNumber.apply(n));
      }
      ```
      참고 : [[이펙티브 자바] 제네릭 Item30 - 이왕이면 제네릭 메서드로 만들어라](https://velog.io/@holidenty/이펙티브-자바-제네릭-Item29-이왕이면-제네릭-타입으로-만들어라)         

<br/>

### 기존 두 가지 방식의 유의사항
- 리플렉션 API 사용시 두 번째 인스턴스 생성 가능해진다.
  - AccessibleObject.setAccessible을 사용해 private 생성자 호출 가능하다.이는 두 번째 객체가 생성되려할 때 예외를 던지는 것으로 방어 가능.

  > **리플렉션 API란?**  
   **구체적인 클래스 타입을 알지 못해도** 그 클래스의 정보(메서드, 타입, 변수 등등)에 접근할 수 있게 해주는 자바 API다.
  >
  > **사용하는 Library, Framework, API, Feature**
  >  <br/>- Jackson, GSON 등의 **JSON Serialization Library**
  >  <br/>- Log4 j2, Logback 등의 **Logging Framework**
  >  <br/>- Apache Commons BeanUtils 등의 **Class Verification API**
  >  <br/>- Spring의 @Autorwired와 같은 **DL, DI** 기능 (*processInject(), inject() Method )*
  >  <br/>- ***Spring Contatiner의 BeanFactory**에서 사용*
  >  <br/>- *내부적으로 Spring의 ReflectionUtils라는 **Abstraction Library**를 사용한다.*
  >  <br/>- Eclipse, Intellij 등의 **IDE,** Junit, Hamcrest와 같은 **Test Framework**
  > <br/>
  > [참고]
  > <br/> - [☕ 누구나 쉽게 배우는 Reflection API 사용법](https://inpa.tistory.com/entry/JAVA-☕-누구나-쉽게-배우는-Reflection-API-사용법)
  > <br/> - [Reflection API (리플렉션)](https://giron.tistory.com/112)
  
<br/>

- 두 가지 방식 중 하나만으로 만들어진 싱글턴 클래스를 직렬화하려면 Serializable을 구현한다고 선언하는 것만으로 부족하다. 직렬화된 인스턴스를 역직렬화할 때마다 새로운 인스턴스가 만들어진다.

  → `readResolve()`메서드를 제공해서 역직렬화 과정에서 만들어진 '가짜 인스턴스'를 대신하여 기존에 생성된 '진짜 인스턴스'를 반환하도록 하면 싱글턴이 보장된다.

  추가 참고 : [자바 직렬화: readResolve와 writeReplace](https://madplay.github.io/post/what-is-readresolve-method-and-writereplace-method)
  > 직렬화(Serialization)란?
  > <br/>
  > : 객체를 데이터 스트림으로 만드는 것을 뜻한다. 다시 얘기하면 객체에 저장된 데이터를 스트림에
  >  쓰기(write)위해 연속적인(serial) 데이터로 변환하는 것을 말한다. 반대로 스트림으로부터
  >  데이터를 읽어서 객체를 만드는 것을 역질렬화(deserialization)라고 한다.
  >  <br/><br/> 참고 : '자바의 정석 3rd - 7.1 직렬화란?' 

<br/>

### 원소가 하나인 열거 타입 방식의 싱글턴

```java
public enum Iphone {
    INSTANCE;

    public void on() {
        System.out.println("The screen is turning on.");
    }
}

public static void main(String[] args){
    Iphone iphone = Iphone.INSTANCE;
    iphone.on();
}
```

- 이전 방식들 보다 더 간결하고, 추가 노력없이 직렬화 가능하다.
- 아주 복잡한 직렬화 상황이나 리플렉션 API를 통해 제2의 인스턴스가 생기는 일을 막아준다.
- **대부분 상황에서 싱글턴을 만드는 가장 좋은 방법**이다. 단, Enum외의 클래스를 상속해야 한다면 이 방법은 사용할 수 없다.

<br/>

## 추가 학습 필요 사항
- 스트림(Stream) 복습
- 리플렉션 API(Reflection API)