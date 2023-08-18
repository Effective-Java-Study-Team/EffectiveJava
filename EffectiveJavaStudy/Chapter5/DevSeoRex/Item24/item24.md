### 아이템 24 - 멤버 클래스는 되도록 static으로 만들라

### 중첩 클래스의 정의와 쓰임새

`중첩 클래스(nested class)` 란 다른 클래스 안에 정의된 클래스를 말한다. <br>
`중첩 클래스`의 용도는 자신을 감싼 `바깥 클래스`에서만 쓰여야 한다. 그 외의 쓰임새가 있다면 `톱레벨 클래스`가 되어야 한다.

### 중첩 클래스의 종류

`중첩 클래스`는 크게 네 가지 종류로 나눌 수 있다.

- **정적(static) 멤버 클래스**

`정적 멤버 클래스`는 다른 클래스 안에 선언되고, 바깥 클래스의 `private 멤버`에도 접근할 수 있다. <br>
`정적 멤버 클래스`는 다른 정적 멤버와 똑같은 접근 규칙을 적용받기 때문에 `private 키워드`를 붙이면 외부에서 접근하지 못하고 `바깥 클래스`에서만 접근할 수 있다.

```java
public class NonStaticMember {

    private void beforePrint() {
        System.out.println("프린트 준비");
    }

    private static class StaticInner {

        int a = 30;

        NonStaticMember staticMember = new NonStaticMember();

        public void print() {
            System.out.println("Inner = " + a);
            staticMember.beforePrint();
//            System.out.println("Outer.a = " + NonStaticMember.this.a); // Compile Error

        }
    }
}
```

`정적 멤버 클래스`는 바깥 클래스의 `private 멤버에` 접근할 수 있지만, `정규화된 this 문법`으로 바깥 클래스의 `인스턴스 멤버`에 접근할 수 없다. <br>
`정적 멤버 클래스`의 좋은 예시는 바깥 클래스와 함께 쓰일 때만 유용한 `public 도우미 클래스`에서 볼 수 있다.

```java
public class Calculator {

    private Calculator() {}

    public enum Operation {
        PLUS("+") {
            public double apply(double x, double y) {
                return x + y;
            }
        },
        MINUS("-") {
            public double apply(double x, double y) {
                return x - y;
            }
        },
        TIMES("*") {
            public double apply(double x, double y) {
                return x * y;
            }
        },
        DIVIDE("/") {
            public double apply(double x, double y) {
                return x / y;
            }
        };

        private final String symbol;

        Operation(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return symbol;
        }

        public abstract double apply(double x, double y);
    }
}
```

계산기 클래스는 계산기가 지원하는 연산 종류를 정의하는 열거 타입을 가지고 있다. <br>
`Operation` 이라는 열거 타입은 Calculator을 사용하는 클라이언트가 `원하는 연산을 참조`하기 위해 `public` 으로 열려있어야 한다.

`열거 타입`이라는 특수한 성질 때문에 static을 붙이지 않아도 `묵시적으로 static`이 붙은 것 처럼 동작한다.

```java
double plus = Calculator.Operation.PLUS.apply(2, 3);
double minus = Calculator.Operation.MINUS.apply(2, 3);
double multiply = Calculator.Operation.TIMES.apply(2, 3);
double divide = Calculator.Operation.DIVIDE.apply(2, 3);
```

보통 `private 정적 멤버 클래스`는 바깥 클래스가 표현하는 객체의 한 부분을 나타낼 때 주로 사용한다. <br>
`Map`의 구현체를 예로 들면, 많은 `Map 구현체`들이 `Entry` 객체들을 가지고 있다.

Entry의 메서드들(`getKey`, `getValue`, `setValue`)는 맵을 직접 사용하지도 않는다. <br>
`엔트리`를 `비정적 멤버 클래스`로 표현하는 것은 `메모리와 시간 낭비`기 때문에 `private 정적 멤버 클래스`로 구현하게 된 것이다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/0dbf1b5d-dde3-4ebe-bdbb-2d24e6e5e093)

`LinkedHashMap`은 `HashMap`의 `Node 클래스`를 상속받아 사용하고, `Node 클래스`는 `Map.Entry`의 구현체다.

만약 멤버 클래스가 `공개된 클래스의 public 또는 protected 멤버`라면 `정적 클래스`인지 여부는 매우 중요해진다. <br>
멤버 클래스까지 `공개 API`가 되기 때문에 향후 릴리스에서 static을 붙이면 `하위 호환성에 문제`가 생긴다.

- **비정적 멤버 클래스**
`비정적 멤버 클래스`는 `static` 키워드가 붙지 않은 멤버 클래스를 말한다.

```java
public class NonStaticMember {

    private int a = 20;

    void print() {
        System.out.println("Outer = " + a);
    }

    class Inner {
        int a = 10;

        public void print() {
            System.out.println("Inner = " + a);
            System.out.println("Outer.a = " + NonStaticMember.this.a);
        }
    }

    private void beforePrint() {
        System.out.println("프린트 준비");
    }

}
```

`비정적 멤버 클래스`의 인스턴스는 바깥 클래스의 인스턴스와 `암묵적으로 연결`되어 있다. <br>
그렇기 때문에 `클래스명.this.접근할 멤버`로 바깥 인스턴스의 `메서드를 호출`하거나 `참조`를 가져올 수 있다.

`중첩 클래스`의 인스턴스가 바깥 인스턴스와 독립적으로 존재할 수 있는 경우에는 `비정적 멤버 클래스`가 아닌 `정적 멤버 클래스`로 만들어야 한다. 

`비정적 멤버 클래스`는 바깥 클래스의 인스턴스 없이는 생성이 불가능하다. <br>
`비정적 멤버 클래스`와 바깥 클래스 사이의 관계는 멤버 클래스가 인스턴스화 될때 굳어져, `변경이 불가능`하다.

관계는 바깥 클래스의 인스턴스 메서드에서 `비정적 멤버 클래스의 생성자`를 호출할때 만들어지지만, <br>
직접 바깥 `인스턴스의 [클래스.ne](http://클래스.ne)w MemberClass(args)를 호출`해 수동으로 만들 수도 있다.

```java
NonStaticMember.Inner inner = nonStaticMember.new Inner();
```

이 관계 정보는 `비정적 멤버 클래스` 안에 만들어져, `메모리 공간`을 차지하게 된다.

`비정적 멤버 클래스`는 주로 어댑터를 정의할 때 자주 쓰이는 편이다. <br>
`Map 인터페이스`의 구현체들은 자신의 컬렉션 뷰를 구현할 때 `비정적 멤버 클래스`를 사용하고 있다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/28b0229c-fc27-4969-a119-853be04a7b18)

`LinkedHashMap`에서 `keySet`을 호출하면 반환하는 `LinkedKeySet`은 `package-private 레벨`의 비정적 멤버 클래스로 구현되어 있다. <br>
내부적으로 `정규화된 this`를 활용한 바깥 클래스의 멤버 접근이 필요하기 때문이 이렇게 구현한 것이다.

```java
public final void clear() { 
    LinkedHashMap.this.clear(); 
}
```

따라서 멤버 클래스에서 `바깥 인스턴스에 접근`할 일이 없다면 무조건 `정적 멤버 클래스`로 만들어야 한다. <br>
`Set`과 `List`와 같은 다른 컬렉션 인터페이스의 구현체들도 `반복자(Iterator)`를 구현할 때 주로 비정적 멤버 클래스를 사용하고 있다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/ccb150b5-c747-4895-afe1-c08e7bd93d88)

`ArrayList`에서 사용하는 `ListItr` 또한 `비정적 멤버 클래스`로 정의되어 있다. <br>
바깥 클래스의 멤버 접근이 필요하기 때문에 `비정적 멤버 클래스`로 정의되어 있는 것이다.

`비정적 멤버 클래스`는 인스턴스로의 `숨은 외부 참조`를 가지기 때문에, `시간과 공간`을 소비해야 한다. <br>
`가비지 컬렉션`이 바깥 클래스의 `인스턴스를 수거하지 못하는 문제`도 생길 위험이 있다.

참조가 눈에 보이지 않기 때문에 문제의 원인을 찾기도 어려워서 심각한 상황이 생길 수도 있다. <br>
`바깥 클래스`의 멤버에 접근하지 않는 `독립적인 존재`라면 반드시 `정적 멤버 클래스`로 만들자.

- **익명 클래스(Anonymous Class)**

`익명 클래스`는 말 그대로 `이름이 없는 클래스`다. <br>
`익명 클래스`는 바깥 클래스의 멤버도 아니다. 멤버와 다르게 `쓰이는 시점`에 선언과 동시에 `인스턴스`가 만들어지는 클래스다.

코드의 어디서든지 만들 수 있다는 특성이 있고, `비정적인 문맥`에서 사용될 때만 바깥 클래스의 `인스턴스를 참조`할 수 있다.

```java
public class AnonymousTest {

    int k = 10;

      static void run() {
        new Dog() {
          @Override
          void balk() {
            System.out.println("k = " + k); // 컴파일 에러 -> 바깥 인스턴스 참조 불가
          }
        }.balk();
      }

        void running() {
          new Dog() {
            @Override
            void balk() {
              System.out.println("k = " + k); // 정상 실행 -> 바깥 인스턴스 참조 가능
            }
        }.balk();
      }
}

public abstract class Dog extends Animal {
    int money = 2000;
    abstract void balk();
}
```

`정적 문맥`에서는 `상수 변수 이외의 정적 멤버`는 가질 수 없다. <br>
`JDK 1.8` 기준으로는 에러가 발생하나, `JDK 16+ 이상의 상위 버전`에서는 가질 수 있는 것으로 확인되었다.

```java
public static void main(String[] args) {

          Dog dog = new Dog() {
            int a = 10;
            int b = 20;
            static int c = 20; -> JDK 1.8 기준으로 컴파일 에러 발생
            static final int d = 30;
            @Override
            void balk() {
              System.out.println("age = " + age);
              System.out.println("money = " + money);
              System.out.println("a + b = " + (a + b));
//            System.out.println("k = " + k);  // 바깥 클래스 참조 불가
            }
        };
    }
```

`익명 클래스`는 선언한 지점에서만 `인스턴스`를 만들 수 있고, `클래스의 이름이 필요한 작업`은 수행할 수 없다. <br>
왜냐하면 클래스의 이름이 `자바파일명${익명객체정의된순번}.class와` 같이 생성되기 때문이다.

```java
Dog dog = new Dog() {
    @Override
    void balk() {}
};

System.out.println(dog.getClass().getName());

출력 : AnonymousTest$1 -> 클래스의 이름이 Dog라고 나오지 않고 익명객체의 네이밍 규칙에 의해 변경됨
```

`instanceof 검사`를 수행할 수 없다고 나오는데, `Dog 타입`을 이용해 익명 객체가 생성되기 때문에 `instanceof 검사`시 true를 반환하고 정상 동작한다.

`instanceof` 검사를 익명클래스를 이용할 일이 있지 않기 때문에 그런 것이 아닐까 생각한다.  <br>
익명 클래스는 클래스의 인스턴스를 선언과 동시에 생성하는 특이한 기법이기 때문에 `일반적인 메커니즘`이 아니다.

`익명 클래스`를 생성할 때는 기존 클래스에 있지 않던 멤버들과 메서드도 추가할 수 있기 때문에,  <br>
클래스를 선언할때의 상태와 다를 수 있어 이렇게 말한 것이 아닌가 생각해본다.

익명 클래스를 사용하는 클라이언트는 `익명 클래스의 타입과, 상위 타입에서 선언한 멤버` 이외에는 호출할 수 없다.

```java
Dog dog = new Dog() {
   int a = 10;
   int b = 20;
   @Override
   void balk() {
      System.out.println("age = " + age);
      System.out.println("money = " + money);
      System.out.println("a + b = " + (a + b));
//    System.out.println("k = " + k);  // 바깥 클래스 참조 불가
    }
};

System.out.println(dog.a); // 호출 불가 -> 컴파일 에러
System.put.println(age); // 호출 가능 -> 상속 받은 멤버
System.out.println(money); // 호출 가능 -> 익명 객체의 타입이 가지고 있던 멤버

public abstract class Animal {

    int age = 20;
}

public abstract class Dog extends Animal {

    int money = 2000;
    abstract void balk();
}
```

익명 클래스를 사용하면 `여러 인터페이스를 구현`하거나 `다른 클래스를 상속`할 수도 없다.  <br>
`익명 클래스`는 표현식 중간에 등장하기 때문에 짧지 않으면 가독성이 매우 떨어진다.

`익명 클래스`는 자바에서 `람다가 등장하기 전`에는 빈번히 사용되었으나, 람다에게 이젠 자리를 물려주었다.  <br>
`익명 클래스`는 `정적 팩터리 메서드`를 구현할때도 사용한다.

- **지역 클래스(Local Class)**

`지역 클래스`는 지역변수를 선언할 수 있는 곳이면 `어디서든 선언`할 수 있고, `Scope` 역시 지역변수와 같다.

```java
public class LocalClass {

    void print() {

        class LocalPrinter {

            static int age = 20; // JDK 1.8버전 기준 컴파일 에러 발생!

            void println() {
                System.out.println("println!");
            }
        }

        LocalPrinter localPrinter = new LocalPrinter();
        localPrinter.println();
    }
}
```

`지역 클래스` 또한  `비정적 문맥`에서만 바깥 인스턴스 참조가 가능하다.  <br>
`정적 멤버`는 가질 수 없고, 가독성을 위해서 익명 클래스처럼 짧게 작성해야 한다.

정적 멤버를 가질 수 없는 제한 역시 `JDK 1.8` 버전에서의 문제이며 `JDK 16+` 이상의 버전에서는 문제가 없다.

- **정리**
1. 멤버 클래스의 인스턴스가 `바깥 인스턴스를 참조`할 경우 `비정적 멤버 클래스`를 사용한다.
2. 멤버 클래스의 인스턴스가 `바깥 인스턴스를 참조하지 않으면` 정적 멤버 클래스를 사용한다.
3. `중첩 클래스`가 `한 메서드 안에서만 사용`되고 인스턴스를 생성하는 지점이 한 곳이고, 해당 타입으로 쓰기에 적합한 클래스나 인터페이스가 있다면 `익명클래스로 만들어 사용`하고 그렇지 않다면 `지역 클래스`로 만들어 사용하자.
