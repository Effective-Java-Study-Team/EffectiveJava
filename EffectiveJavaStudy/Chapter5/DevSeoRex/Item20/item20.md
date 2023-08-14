### 아이템 20 - 추상 클래스보다는 인터페이스를 우선하라

### 자바에서 제공하는 다중 구현 메커니즘

- **추상 클래스**
추상 클래스는 정의한 타입을 구현하는 클래스가 반드시 `추상 클래스의 하위 클래스`가 되어야한다. <br>
자바의 `단일 상속 특성`으로 인해 추상 클래스 방식은 `새로운 타입을 정의하는 데 제약`이 있다.

- **인터페이스**
자바 8부터 인터페이스도 `디폴트 메서드`를 제공할 수 있어, `인스턴스 메서드`를 구현 형태로 제공할 수 있다. <br>
인터페이스가 선언한 메서드를 모두 정의하면, 어떤 클래스를 상속했는지와 상관없이 `같은 타입으로 취급` 된다.

### 인터페이스의 강력한 장점

- **기존 클래스에도 손쉽게 새로운 인터페이스 구현이 가능하다.**

인터페이스가 요구하는 `메서드를 오버라이딩` 하고, `implements`구문만 추가하면 간단히 구현이 가능하다. <br>
추상 클래스의 경우 `하나의 클래스만 상속` 받을 수 있기 때문에 여러 추상 클래스의 기능이 필요하다면, 문제가 생긴다.

- **여러 클래스가 한 추상 클래스를 확장한다면 클래스 계층 구조에 혼란을 가져온다.**

예를 들어 두 클래스가 `하나의 추상 클래스를 확장`하길 원한다면, 두 클래스가 한 추상 클래스를 조상으로 두게 된다. <br>
이 방식은 새로 추가된 추상 클래스의 모든 자손이 이를 상속하게 되는 문제가 있다.

```java
public abstract class Animal {

    /*
    *   동물이 할 수 있는 모든 동작을 정의한 클래스
    *   -> 소리를 내는 기능
    *   -> 걷는 기능
    *   -> 나는 기능
    * */
    abstract void makeSound(String sound);
    abstract void walk();
    abstract void fly();
}

public class Dog extends Animal {
    @Override
    void makeSound(String sound) {
        System.out.println("DogSound = " + sound);
    }

    @Override
    void walk() {
        System.out.println("mixin.Dog is walking...");
    }

    /*
    *   개는 날 수 없지만 mixin.Animal 클래스를 상속받음으로 인해서
    *   클래스의 의미가 변질된다.
    * */
    @Override
    void fly() {
        System.out.println("mixin.Dog can fly??");
    }
}

public class Duck extends Animal {

    @Override
    void makeSound(String sound) {
        System.out.println("mixin.Duck Sound = " + sound);
    }

    @Override
    void walk() {
        System.out.println("mixin.Duck is walking...");
    }

    @Override
    void fly() {
        System.out.println("mixin.Duck is Fly!!");
    }
}
```

Animal 클래스는 동물이 할 수 있는 모든 동작을 추상화한 추상 클래스다. <br>
동물은 조류라면 날수도 있고, 걸을 수도 있을 것이다. 하지만 `조류가 아닐 경우에는 날 수 없다.`

동물의 종류와 관계 없이 할 수 있는 동작들을 모두 메서드로 가지고 있어서, 개는 날 수 없음에도 fly 메서드를  <br>
`강제로 오버라이딩` 해서 확장하게 된 것을 볼 수 있다.

Animal 클래스로 모든 동물의 동작을 추상화하는 것이 아니라, `인터페이스로 동물들의 행동을 정의`했다면 위와 같은 문제는 생기지 않았을 것이다.

```java
public interface Flyable {
    void fly();
}

public interface Walkable {
    void walk();
}

public class Horse implements Walkable {
    /*
    *   말 클래스는 걷는 것만 가능하기 때문에 걷는 동작만 정의한다.
    *   mixin.Animal 클래스를 상속받았다면 나는 동작이 없음에도 강제로 상속받아야 하는 문제가 생긴다.
    * */
    @Override
    public void walk() {
        System.out.println("mixin.Horse is walk");
    }
}

public class Eagle implements Flyable, Walkable {
    /*
    *   독수리는 날고, 걷는 것이 모두 가능하기 때문에
    *   동작을 정의한 인터페이스를 여러개 구현하여 유연하게 설계할 수 있다.
    * */
    @Override
    public void fly() {
        System.out.println("mixin.Eagle is Fly");
    }

    @Override
    public void walk() {
        System.out.println("mixin.Eagle is Walk");
    }
}
```

나는 것과 걷는 것을 모두 할 수 있는 독수리는 `Flyable, Walkable 인터페이스를 모두 구현`했고, <br>
걷는 것만 할 수 있는 말 클래스는 `Walkable만 구현`함으로서 Dog 클래스가 fly 메서드를 강제로 오버라이딩 했던 것을 개선했다.

- **mixin 정의(믹스인 인터페이스)**
믹스인이란 구현한 클래스에 원래의 `주된 타입` 외에도 특정 선택적 행위를 제공한다고 선언하는 효과를 준다. <br>
`Comparable`은 자신을 구현한 클래스의 인스턴스끼리는 순서를 정할 수 있다고 선언하는 것과 같다.

추상 클래스로는 믹스인을 정의할 수 없다. <br>
믹스인은 특정 선택적 행위를 제공하기 위해 `추상 클래스`또는 `인터페이스`를 클래스에 덧씌워야 하는데 이런 경우에 추상 클래스를 사용할 경우 여러 클래스를 `다중 상속할 수 없는 문제`가 생긴다.

### 계층 구조가 없는 타임 프레임워크

인터페이스로는 계층구조가 없는 `타입 프레임워크`를 잘 만들 수 있다. <br>
타입을 계층적으로 정의하면 개념을 구조적으로 잘 표현할 수 있다는 장점이 있지만, `계층을 엄격하게 구분하기 어려운 개념`도 있기 때문이다.

```java
public interface BackendAble {
    /*
    *   Server API 만 개발할 수 있는 백엔드 개발자
    * */
    void makeServerApi();
}

public interface FrontendAble {
    /*
    *   사용자 View 만 만들 수 있는 프론트엔드 개발
    * */
    void makeView();
}
```

`백엔드 개발과 프론트 엔드` 개발을 할 수 있는 사람이 각각 있다고 해보자. <br>
`백엔드 개발과 프론트 엔드` 개발을 모두 할 수 있는 `풀스택 개발자`도 간혹 존재한다.

그렇다면 이런 경우 어떻게 타입을 정의할 수 있을까? <br>
물론 `BackendAble과 FrontendAble 인터페이스`를 모두 구현하는 방법도 있겠지만, 두 인터페이스를 모두 확장하여 새로운 제 3의 인터페이스를 정의할 수도 있다.

```java
public interface FullStackAble extends FrontendAble, BackendAble {
    /*
    *   백엔드와 프론트엔드 개발이 전부 가능한 풀스택 개발자도 있을 수 있다.
    *   백엔드와 프론트를 한번에 개발해 웹 서비스를 만드는 메서드를 추가 정의했다.
    * */
    void makeWebService();
}
```

풀스택 개발자라면 `FullStackAble 인터페이스`하나만 구현하면 되는 것이다.

이렇게 여러 속성을 조합해서 사용해야 하는 경우에 `2ⁿ` 개만큼 조합의 개수가 나온다. <br>
이런 구조를 클래스로 만들려면 소위 말하는 `조합 폭발(combinatorial explosion)`에 직면하게 된다.

```java
public abstract class FullStack {

    /*
    *   모든 조합에 대해 메서드를 가지고 있어야 한다.
    *   조합 폭발(combinatorial explosion)이 일어난다.
    * */
    abstract void makeServerApi();
    abstract void makeView();
    abstract void makeWebService();
}
```

인터페이스는 `기능을 향상 시키는 가장 안전하고 강력한 수단`이다.

상속해서 만든 클래스는 래퍼 클래스보다 활용도가 떨어지고 깨지기 더 쉽다.

### 인터페이스의 디폴트 메서드

Java 8 부터 인터페이스는 디폴트 메서드를 제공할 수 있게 되었다. <br>
구현 방법이 명백하고 하위 클래스에서도 재정의 하지 않을 확률이 높은 메서드는 디폴트 메서드로 제공할 수 있다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/f2efabda-5f0f-4ad2-b8ee-6974dfbe2cd4)

`Collection` 인터페이스의 디폴트 구현이 되어있는 `removeIf` 메서드다. <br>
디폴트 메서드는 상속하려는 사람들을 위해  `@implSpec` 자바독 태그를 붙여 문서화해야 한다.

디폴트 메서드에도 제약은 존재한다. <br>
equals 와 hashCode 같은 Object의 메서드를 디폴트 메서드로 제공해서는 안된다.

구현체마다 가지고 있는 필드가 다르거나 `논리적 동치를 결정하는 조건`이 다를 수 있기 때문이다. <br>
또한 `public이 아닌 정적 멤버`를 가질 수 없고, `인스턴스 필드`를 가지지 못한다는 것이 인터페이스의 한계점이다.

### 인터페이스의 추상 골격 구현(skeletal implementation) 클래스

`추상 골격 구현 클래스`란 인터페이스와 추상 클래스의 장점을 모두 취할 수 있는 방법이다.

인터페이스로는 타입을 정의하고, 필요하면 디폴트 메서드를 몇 개 제공한다. <br>
골격 구현 클래스는 나머지 `메서드를 전부 정의`하고, 필요하다면 `인스턴스 필드`와 `정적 필드`등을 선언해 사용할 수 있다.

단순히 골격 구현 클래스를 확장하게 되면, 인터페이스를 구현하는 데 필요한 일이 대부분 끝난다. <br>
이런 패턴을 `템플릿 메서드 패턴`이라고 한다.

- **템플릿 메서드 패턴**

```java
public abstract class AbstractStep {

    /*
    *   step1, 2, 3는 공통적으로 사용하는 메서드지만
    *   상속받은 하위 클래스마다 중복적으로 재정의 해야한다.
    * */

    void setup() {
        step1();
        step2();
        step3();
        step4();
        step5();
    }

    abstract void step1();
    abstract void step2();
    abstract void step3();
    abstract void step4();
    abstract void step5();
}

public class SetupFirst extends AbstractStep {
    @Override
    void step1() {
        System.out.println("step1");
    }

    @Override
    void step2() {
        System.out.println("step2");
    }

    @Override
    void step3() {
        System.out.println("step3");
    }

    @Override
    void step4() {
        System.out.println("setup first = step4");
    }

    @Override
    void step5() {
        System.out.println("setup first = step5");
    }
}

public class SetupSecond extends AbstractStep {

    @Override
    void step1() {
        System.out.println("step1");
    }

    @Override
    void step2() {
        System.out.println("step2");
    }

    @Override
    void step3() {
        System.out.println("step3");
    }

    @Override
    void step4() {
        System.out.println("setup second = step4");
    }

    @Override
    void step5() {
        System.out.println("setup second = step5");
    }
}
```

SetupFirst와 SetupSecond의 메서드 구현을 보면 `step1~step3` 까지의 구현이 전부 일치한다. <br>
그렇다면 step1~step3 까지 `코드의 중복이 발생`했다는 뜻이다.

```java
public abstract class RefactorAbstractStep {
    
    /*
    *   하위 클래스에서 공통으로 사용하는 부분은
    *   디폴트 구현을 제공해서, 중복된 코드를 작성하지 않아도 된다.
    *   -> 템플릿 메서드 패턴
    * */

    void setup() {
        step1();
        step2();
        step3();
        step4();
        step5();
    }

    protected void step1() {
        System.out.println("step1");
    }

    protected void step2() {
        System.out.println("step2");
    }

    protected void step3() {
        System.out.println("step3");
    }
    abstract void step4();
    abstract void step5();
}

public class RefactorSetupFirst extends RefactorAbstractStep {

    @Override
    void step4() {
        System.out.println("setup first = step4");
    }

    @Override
    void step5() {
        System.out.println("setup first = step5");
    }
}

public class RefactorSetupSecond extends RefactorAbstractStep {

    @Override
    void step4() {
        System.out.println("setup second = step4");
    }

    @Override
    void step5() {
        System.out.println("setup second = step5");
    }
}
```

하위 클래스에서 공통으로 사용하는 step1 ~ step3 메서드를 추상 클래스에서 `디폴트 구현` 해주고, <br>
하위 클래스는 step4 ~ step5 메서드만 오버라이딩 하게 함으로서 `코드 중복을 제거`했다.

템플릿 메서드는 `알고리즘의 구조는 공통적`이나 구체적인 단계가 다를때 사용하는 패턴이다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/5ada187c-d90e-4ba6-be49-adfe1aebcaf6)

위의 코드를 `인터페이스를 사용한 방법`을 변경해보면 아래와 같다.

```java
public interface Jobs {

    default void setup1() {
        System.out.println("setup1");
    }

    default void setup2() {
        System.out.println("setup2");
    }

    default void setup3() {
        System.out.println("setup3");
    }

    void setup4();
    void setup5();

}

public abstract class AbstractJob implements Jobs {

    int a;
    int b;

    void set() {
        setup1();
        setup2();
        setup3();
        setup4();
        setup5();
    }

    public abstract void setup4();
    public abstract void setup5();
}

public class FirstJob extends AbstractJob {

    @Override
    public void setup4() {
        System.out.println("FirstJob = setup4");
    }

    @Override
    public void setup5() {
        System.out.println("FirstJob = setup5");
    }
}

public class SecondJob extends AbstractJob {
    @Override
    public void setup4() {
        System.out.println("Second Job = setup4");
    }

    @Override
    public void setup5() {
        System.out.println("Second Job = setup5");
    }
}
```

`인터페이스`는 공통적으로 사용되는 기능의 `디폴트 구현을 제공`하고, 추상 골격 구현 클래스는 `메서드를 구현`하는데 필요한 인스턴스 필드를 추가로 정의하거나 다른 작업들을 명시한다. <br>
이런 추상 골격 구현 클래스는 Java 에서도 찾아볼 수 있는데, `Abstract 키워드가 앞에 붙은 컬렉션`들이 그 예시다.

- **추상 골격 구현 클래스 목록**
    1. `AbstractCollection`
    2. `AbstractSet`
    3. `AbstractList`
    4. `AbstractMap`

```java
static List<Integer> intArrayAsLlist(int[] a) {

    Objects.requireNonNull(a);

    return new AbstractList<>() {
    	@Override
      public Integer get(int i) {
          return a[i];
    	}
    
    	@Override
      public Integer set(int i, Integer val) {
          int oldVal = a[i];
          a[i] = val;
          return oldVal;
    	}
    	
    
    	@Override
      public int size() {
          return a.length;
    	}
   }
}
```

이 메서드는 int 배열을 받아, Integer 인스턴스의 리스트 형태로 반환해주는 역할을 한다. <br>
이 메서드가 하는 역할은 배열을 List로 변경해주는 `어댑터 역할`을 한다고 볼 수 있다.

- **어댑터 패턴**

어댑터 패턴이란 `서로 호환되지 않는 인터페이스`들을 동작시키기 위해 중간에서 호환되도록 변환해주는 것을 뜻한다. <br>
어댑터 패턴을 사용하면 기존 클래스의 코드를 변경하지 않고도, 호환되지 않는 클래스들을 함께 동작하도록 할 수 있다.

```java
public class Volt110 {

    public static final int volt = 110;

    private Volt110() {}

    public static Volt110 getInstance() {
        return new Volt110();
    }
}

public class Volt220 {

    public static final int volt = 220;

    private Volt220() {}

    public static Volt220 getInstance() {
        return new Volt220();
    }
}

public interface Connection {

    static void connect(Volt220 volt220) {
        Objects.requireNonNull(volt220);

        System.out.println(volt220.volt + " 연결 성공");
    }
}

Volt110 volt110 = Volt110.getInstance();
Connection.connect(VoltAdapter.translateVolt110ToVolt220(volt110));

// 출력
110 Volt change to 220 Volt
220 연결 성공
```

Connection 클래스는 `220 볼트만 지원하는 멀티탭`이라고 가정한다. <br>
110 볼트로 220 볼트를 지원하는 멀티탭은 사용하기가 어렵다. 따라서 VoltAdapter 객체가 중간에서 110 볼트를 220 볼트로 변환해주기 때문에 사용이 가능해진다.

`서로 호환되지 않는 인터페이스`인 Connection과 110 볼트를 연결해주는 어댑터 클래스로 VoltAdapter를 추가한 것이다.

- **골격 구현 클래스를 우회적으로 사용하기**

골격 구현 클래스를 우회적으로 이용할 수 있는 방법도 있다.

클래스의 구조상 골격 구현을 확장하지 못하는 처지일 경우 인터페이스를 직접 구현해야 한다. <br>
이럴 경우에라도 인터페이스가 직접 제공하는 `디폴트 메서드의 이점`은 누릴 수 있고, 구현해야 하는 메서드를 <br>
골격 구현 클래스에서 `이미 구현한 것을 호출`하는 것으로 사용할 수 있다. 

```java
public interface TouchAble {
    void touch();
    void updateTouch();
}

public abstract class AbstractTouchScreen implements TouchAble {

    @Override
    public void touch() {
        System.out.println("touch!!");
    }

    public abstract void updateTouch();
}

public interface Computer {

    void on();
    void off();
}

public abstract class AbstractNotebook implements Computer {

    @Override
    public void on() {
        System.out.println("on");
    }

    @Override
    public void off() {
        System.out.println("off");
    }

    abstract void charge();
}

public class TouchNoteBook extends AbstractNotebook implements TouchAble {

    private final TouchScreen touchScreen = new TouchScreen();

    @Override
    void charge() {
        System.out.println("charging start ... !");
    }

    @Override
    public void touch() {
        touchScreen.touch();
    }

    @Override
    public void updateTouch() {
        touchScreen.updateTouch();
    }

    private class TouchScreen extends AbstractTouchScreen {

        @Override
        public void updateTouch() {
            System.out.println("touch update");
        }
    }
}
```

`TouchNotebook`은 이미 `AbstractNotebook`을 상속받고 있기 때문에 더 이상 클래스를 추가 상속 받을 수 없는 상태다. <br>
노트북의 기능을 다하고 있지만 터치 기능까지 수행을 해야되기 때문에 `AbstractTouchScreen`클래스를 확장해야 하지만 이미 상속할 수 있는 클래스의 개수는 다 찼다.

이런 경우 우회할 수 있는 방법이 있다. <br>
`TouchAble 인터페이스를 구현`하고, 골격 구현을 확장한 클래스를 `private 내부 클래스로 정의`하는 것이다.

재정의 해야하는 메서드에서는 `내부 클래스`의 `인스턴스의 메서드 호출`로 대신할 수 있게 된다. <br>
이렇게 하면 `시뮬레이트한 다중 상속(simulated multiple inheritance)` 으로 다중 상속의 많은 장점을 제공하고 단점은 피하게 된다.

### 추상 골격 클래스 작성에서 주의할 점

추상 골격 클래스를 작성하려면 인터페이스를 잘 살펴보고 다른 메서드들의 구현에 사용되는 기반 메서드들을 선정해야 한다.

기반 메서드들은 골격 구현에서는 추상 메서드가 된다. <br>
기반 메서드들을 사용해 직접 구현할 수 있는 메서드를 모두 디폴트 메서드로 제공한다.

Object의 equals와 hashCode 같은 메서드는 `디폴트 메서드로 제공`해서는 안된다. <br>
추상 메서드로 선언하여 하위 클래스에게 `반드시 재정의하도록 강제`하는 것은 가능하다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/443d5fce-617b-4e75-8007-bde072b2590b)

Java 의 `Map 인터페이스`도 equals와 hashCode를 재정의 하도록 강제하는 것을 볼 수 있다. <br>
골격 구현은 기본적으로 `상속해서 사용`하는 걸 염두하고 있기 때문에 아이템 19에서 살펴보았던 상속을 염두할때의 `설계 및 문서화 지침`을 모두 따라야 한다.

- **단순 구현(simple implementation)**

단순 구현은 골격 구현의 변종이다. <br>
단순 구현도 골격 구현과 같이 `상속을 위해 인터페이스를 구현한 것`이지만, `추상 클래스가 아니란 점`이 다르다.

단순 구현은 동작하는 `가장 단순한 구현방법`이다. <br>
단순 구현은 그대로 써도 되고 `필요하다면 확장`해도 된다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/4dda434d-1c9c-49c6-a07d-b105059a6495)

- **정리**
    1. 다중 구현용 타입으로는 `인터페이스가 가장 적합`하다.
    2. `복잡한 인터페이스`라면 구현하는 수고를 덜어 줄 `골격 구현을 함께 제공`하는 방법을 고려해보자.
    3. 골격 구현은 ‘가능한 한’ `인터페이스의 디폴트 메서드`로 제공하라.
    4. ‘가능한 한’ 제공하라는 이유는 인터페이스에 걸려 있는 구현상의 제약 때문에 `골격 구현을 추상 클래스`로 제공하는 경우가 흔하기 때문이다.
