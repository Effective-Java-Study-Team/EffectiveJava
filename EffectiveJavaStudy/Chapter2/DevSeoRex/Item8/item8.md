### 아이템 8 - finalizer와 cleaner 사용을 피하라

- **자바는 두 가지 객체 소멸자를 제공한다.**
    - finalizer cleaner 두 가지 소멸자를 제공한다.

기본적으로 finalizer는 쓰지 말아야 한다. <br>
예측할 수 없고, 상황에 따라 위험할 수 있어 일반적으로 불필요하다.

자바 9부터 finalizer가 deprecated 됨에 따라 cleaner가 등장 했지만 여전히 예측할 수 없고 느리다. <br>
자바에서는 try-with-resources 또는 try-finally를 사용해 비메모리 자원을 회수한다.

finalizer와 cleaner는 제때 실행되어야 하는 작업은 절대 할 수 없다. <br>
언제 실행될지 예측할 수 없고, 상황에 따라 위험할 수 있기 때문이다.

자바 언어 명세는 어떤 스레드가 finalizer를 수행할지 명시하지 않으니 이 문제를 예방할 보편적인 해법은 존재하지 않는다.

cleaner는 자신을 수행할 스레드를 제어할 수 있다는 면에서 조금 낫다. <br>
하지만 여전히 백그라운드에서 수행되기 때문에 가비지 컬렉터의 통제하에 있으니 즉각 수행된다는 보장은없다.

자바 언어 명세는 finalizer와 cleaner의 수행 시점 뿐만 아니라 수행 여부조차 보장하지 않는다. <br>
**finalizer 동작 중 발생한 예외는 무시되며,** 처리할 작업이 남아도 그 순간 종료되어 버린다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/d9e53e97-b1be-4d90-8705-b7a351de5dec)

클래스를 하나 만들고 finalize 메서드를 오버라이딩 해주었다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/aff69213-58a6-4eb1-b7aa-68d9a29d4422)

메인 메서드에서 객체를 생성하고 강한 참조를 끊은 뒤 System.gc를 호출해보면 아래와 같은 결과가 나온다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/09c17386-5a4e-46b7-b6ef-eacec1c62127)

객체 소멸 시작을 콘솔에 출력하고, throwException에 의해 예외가 발생했으나 무시되고 **경고조차 출력되지 않는다.**

Cleaner를 사용한 라이브러리는 그나마 이런 문제는 발생하지 않는다.

```java
public class Room implements AutoCloseable {
    private static final Cleaner cleaner = Cleaner.create();

    private static class State implements Runnable {
        int numJunkPiles;

        State(int numJunkPiles) {
            this.numJunkPiles = numJunkPiles;
        }

        @Override
        public void run() {
            System.out.println("방 청소");
            numJunkPiles = 0;
        }
    }

    private final State state;

    private final Cleaner.Cleanable cleanable;

    public Room(int numJunkPile) {
        state = new State(numJunkPile);
        cleanable = cleaner.register(this, state);
    }

    @Override
    public void close() {
        System.out.println("호출");
        throwException();
        cleanable.clean();
        System.out.println("호출 완료");
    }

    private void throwException() {
        throw new RuntimeException("예외가 발생했다!");
    }
}
```

Cleaner를 사용해 자원을 회수하는 코드를 작성했다. <br>
AutoCloseable 인터페이스를 구현해서 try-with-resources로 자원을 회수할 수 있도록 했다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/e1f750b6-d810-497b-846f-550e1d96be6b)

이 코드를 실행하면 AutoCloseable의 구현체이기 때문에 close 메서드가 자동으로 호출된다.
![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/ec75b364-18d5-49d5-9693-96267f8744b0)

Cleaner를 사용하면 사용하는 스레드를 제어하기 때문에 finalize와 다르게 예외를 확인할 수 있다. <br>
그렇다면 예외를 잡아서 처리할 수도 있을 것이다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/f83a8f5b-55dd-4231-8a2e-e0c59ba66d4d)

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/51e66142-b7b7-43be-80ff-4ae5fb770d81)

AutoCloseable한 객체를 생성하고 try-with-resources로 자원을 회수했을때 finalizer나 cleaner보다 <br>
성능이 더 좋았다.

- **finalizer를 이용한 finalizer attack** 

finalizer를 이용한 **finalizer attack**이 들어온다면 정적 필드에 자신을 할당하여 가비지 컬렉터의 수집 대상이 되지 않도록 할 수 있다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/4c96f7d2-a903-4475-a887-b40b0f8b909f)

생성자나 직렬화 과정에서 예외가 발생하면 생성되다 만 객체에서 악의적인 하위 클래스의 finalizer가 수행 될 수 있다. <br>

finalize 메서드를 악의적으로 오버라이딩 해서 유효성 검사를 무력화시키고 GC로 부터 수집당하지 않도록 만들수도 있다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/c6387a9a-9018-43b1-a515-d6dca92b496b)

음수가 value에 할당되게 되면, 예외가 발생하고 인스턴스가 생성되지 않아야 정상이다.<br>
하지만 오버라이딩 해둔 finalize 메서드때문에 잘못된 값을 이용해 생성자를 호출해도 인스턴스는 생성된다.

왜 그럴까?

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/56924840-1fdb-4420-acaf-83327ae01834)

Zombie 객체의 생성자에 음수를 할당하고 <br>
System.gc와 System.funFinalization 메서드를 호출한 뒤 스레드를 1초 멈춘다.

그렇다면 Zombie 인스턴스는 존재하지 않기 때문에 NullPointerException이 발생해야한다.
![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/a826259a-a9fe-4edb-b587-aeeedf45dcad)

하지만 Zombie 인스턴스가 존재하고 있는 것을 확인할 수 있다. <br>
왜 value는 0으로 되어 있을까?

Zombie 인스턴스의 생성자에서 예외가 발생하기 때문에 객체도 생성되지 않고, 그로인해 GC의 대상이 되지 않을거라고 생각했다. 

그래서 코드를 변경해서 실행해보았다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/618cb3a1-b3bd-428a-8e3a-4594a65060cb)

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/b3b1ac03-3a48-4d7d-9b3e-3894ff655f4b)

이 코드를 실행해보고 알 수 있는 건 생성되다 만 객체가 존재하기 때문에 GC는 해당 객체를 수거하게 되고 <br>
runFinalization 메서드를 호출함으로서 수집 대상 객체의 finalize를 호출하게 되기 때문에 finalize 메서드 안에서 static 필드를 초기화한다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/505e8019-1b82-4304-ad1f-5d830f2654b5)

코드를 실행해보면 최초 생성자가 호출되면 만들다 만 객체의 주소를 확인할 수 있고, <br>
finalize 블럭이 호출될때 호출되는 객체의 주소와, static 필드에 초기화 된 객체의 주소가 모두 같다.

즉 만들다 만 객체란 value의 값이 초기화 되지 못하고 생성자 호출도중 예외가 발생하여 value의 값이 0인 객체를 말한다.

만들다 만 객체지만 주소값이 있기 때문에 메모리에 할당이 되어 있는 상태이므로 GC의 대상이 된다. <br>
따라서 GC가 동작한뒤, runFinalization 메서드 호출에 의해 수집대상 객체의  finalize 를 호출하고 static 필드에 만들다 만 객체의 주소를 대입한다.

이렇게 해서 만들다 만 객체가 정적 변수에 할당 됨으로서 GC되지 않는 문제가 발생하게 되는 것이다.

대부분 이렇게 코드를 작성하지는 않는다. <br>
하위 클래스에서 finalize를 재정의 해서 공격하는 사례도 있는데 이건 어떻게 이루어지는지 확인해본다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/1a9af4d2-e8c8-43fe-95d8-be772ea67324)

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/de5ceb80-9dc9-4460-8136-ae43055d8a87)

SuperClass와 SubClass를 정의했다. SubClass는 생성자 안에서 부모의 생성자를 명시적으로 호출한다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/52c2fc76-9e89-4819-a13b-c052ee0ee64b)

음수 value를 생성자에 인자로 넣어 호출하게 되면 SuperClass의 생성자를 호출하는 도중 예외가 발생한다. <br>
이 경우에도 예외가 전파되어 만들다 만 객체가 생성되게 된다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/1b5a4ae8-f6eb-4cb4-98d9-4a5eee118d37)

따라서 GC가 동작하면서 finalize 메서드를 호출하게 되면서 만들다 만 객체가 수집되지 않고 finalizer attack이 성공하게 된다.

이걸 어떻게 막을 수 있을까?

JLS(Java Language Specification) 3rd Edition 까지는 세가지 방법을 제안했다.

- **initialized flag**
    - 객체가 정상적으로 생성되었을때 **flag 변수를 셋팅**해서 메서드 실행시 유효한 객체인지 확인하는 방법이다.
    - 이 기법은 번거롭고 생략될 수 있다는 점에서 안정성이 좋지 못한 방법이다.
- **preventing subclssing**
    - 클래스 자체를 final로 만들어 **상속을 막아버리는 방법**이다.
    - 이 방법은 손쉽게 finalizer attack을 막을 수 있다는 장점이 있지만 **확장성을 잃는다는 치명적인 단점**이있다.
- **create a final finalizer**
    - 아무일도 하지 않는 finalize 메서드를 final로 오버라이딩 하는 방법이다.
    - 이 문제는 **finalize를 오버라이딩 하게 되면 finalize 를 오버라이딩 하지 않은 객체보다 생존기간이 길어진다.**
    - 그 이유는 객체의 레퍼런스가 끊기면 GC에 의해 수집되어야 하는데 finalize가 오버라이딩 되어 있다면 finalize가 실행되기 전에 수집되지 않는다.
    - 자바 언어 스펙을 보면 finalize가 어떤 스레드에 의해 실행될지, 언제 실행되는지 실행은 되는지 하나도 명확한 부분이 없다.
 
그렇다면 세가지 방법은 좋지 못한 방법인 것 같다. <br>
그래서 JLS는 네 번째 방법을 제안했다.

모든 클래스는 상위 클래스인 부모의 생성자를 호출하게 되어있다. <br>
따라서 java.lang.Object의 객체가 생성되기 전에 예외를 발생시키면 finalize를 호출할 수 없게 된다.

이 부분은 Object가 생성될 때 어떤 순서로 생성되는지 정확하게 이해해야 한다.

1. 객체를 위한 공간을 할당한다.
2. 객체의 모든 인스턴스 변수를 기본값으로 설정. 여기에는 객체의 수퍼 클래스에 있는 인스턴스 변수가 포함된다.
3. 객체에 대한 파라미터 변수를 지정한다.
4. 명시적 또는 암시적 생성자 호출(생성자에서 this() 또는 super() 호출)을 처리한다.
5. 클래스의 변수를 초기화한다.
6. 생성자의 나머지 부분을 실행한다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/fd2e7180-99c3-4053-96b2-99b9bd012560)

외부에서 사용하는 Super 클래스 생성자 내부에서 명시적으로 this() 생성자 호출을 먼저 해준다. <br>
그러면 private SuperClass() 생성자에서 생성자의 코드가 수행되기 전에 생성자의 파라미터를 먼저 <br>
처리하게 되는데 그때 validate 메서드가 수행되며 예외가 발생하므로, super() 생성자는 수행되지 않는다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/fd01c0e5-79bf-41f0-a6b5-29f966a796b3)

이 코드를 수행하면 만들다 만 SubClass 객체도 없기 때문에 attack failed가 출력되게된다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/f2f88cfa-5038-4b59-8cf4-02290f86b0ad)

Java의 상속 관계에서 자식 생성자에서는 명시적으로 this 생성자 호출이나, super 생성자를 호출해야 <br>
하는데, 그 이유는 부모 객체부터 생성이 된 후에 자식 객체가 생성되기 때문에 그렇다.

Zombie 클래스에서 finalizer attack을 시도했을때와 결과가 다른 이유는 아래와 같다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/64544474-8ed5-4b1d-8d7b-ad874ed9cbca)

컴파일러는 명시적으로 super 생성자를 호출하지 않아도 알아서 넣어주기 때문에,

객체 생성 순서에 의해 생성자의 코드를 처리하기 전, int value 파라미터를 먼저 처리하게 되고 파라미터를 <br>
처리하는 부분에서는 예외가 발생하지 않아 super 생성자 호출을 통해 Object 객체가 생성되게 된다.

따라서 생성자 내부 코드가 실행되기 때문에 잘못된 value의 값을 입력하여 예외가 발생하더라도 만들다 만 Zombie 객체는 생성되게 된다.

SubClass의 경우 Object 클래스가 생성되지 못해 SuperClass도 생성되지 못하고 <br>
그 여파로 **SubClass 또한 생성되지 못하여 만들다 만 SubClass 객체조차 생성되지 않는 것이다.**

finalizer를 사용하는게 성능이나 안정성에서 문제만 주는 것 같아 이걸 어디에 쓰는지 궁금해졌다.<br>
이 부분은 자바 라이브러리의 일부 클래스를 보면 알 수 있는데 대표적인 클래스인 FileOutputStream을 <br>
보면 안전망 역할의 finalizer를 제공한다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/ecd30846-b523-4b53-aab8-0ecc45539c53)

FileOutputStream 클래스를 어떤 메서드나 로직에서 사용하고 그 Scope가 다했을때 FileOutputStream 객체가 GC에 의해 수집되게 되고 <br> 
FileOutputStream은 내부에 AltFinalizer 타입의 변수를 가지고 있으므로 <br>
**AltFinalizer가 가비지 컬렉션 대상이 되어 finalize가 수행될때 close 메서드를 호출해서 스트림을 닫게된다.**

명시적으로 close를 호출하지 않을 경우 finalizer를 통해 늦게나마 자원이 회수되기를 기대할 수 있게 된다. <br>
안정망 기능이 개발되어 있기는 하지만 이 클래스는 **AutoCloseable 인터페이스를 구현**했기 때문에

try - with - resources를 이용한 자원 정리가 가장 깔끔하다.

그렇다면 cleaner와 finalizer가 나서서 처리할 작업은 어떤 것이 있을까? <br>
네이티브 피어와 연결된 객체를 해제할 때 cleaner와 finalizer를 사용할 수 있다.

성능 저하를 감당할 수 있고, 네이티브 피어가 심각한 자원을 가지고 있지 않을 때에만 해당한다.

**네이티브 피어가 사용하는 자원을 즉시 회수해야 한다면 close 메서드를 사용해야 한다.** <br>
네이티브 피어는 자바 객체가 아니기 때문에 가비지 컬렉터는 그 존재를 알지 못해서 직접 회수해 줘야한다.

먼저 AutoCloseable 인터페이스를 구현한 간단한 방법을 살펴보자.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/48b0131a-913e-4258-89e6-b10b1df338d7)

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/85cc0383-0bb0-4843-bb39-355926e1d893)

이 방법을 사용하면 간단히 try - with - resources 구문으로 문제를 해결할 수 있다.

**Cleaner를 내부적으로 구현해 안전망 역할을 하도록 할 수도 있다.**

```java
public class NativeResource implements AutoCloseable {
    private final long nativePeer;

    private static final Cleaner cleaner = Cleaner.create();

    private final Cleaner.Cleanable cleanable;


    public NativeResource() {
        this.nativePeer = initNativePeer();
        this.cleanable = cleaner.register(this, new NativeResourceCleaner(this.nativePeer));
    }

    private static class NativeResourceCleaner implements Runnable {
        private long nativePeer;

        public NativeResourceCleaner(long nativePeer) {
            this.nativePeer = nativePeer;
        }

        @Override
        public void run() {
            // 네이티브 피어를 사용하여 자원을 안전하게 해제하는 작업 수행
            releaseNativePeer(this.nativePeer);
        }
    }

    private native long initNativePeer();
    private static native void releaseNativePeer(long nativePeer);

    @Override
    public void close() {
        cleanable.clean();
    }
}
```

Cleaner를 구현해주면 코드는 복잡해지지만 명시적으로 close를 호출해주지 않거나 try - with - resources <br>
구문을 사용하지 않을 경우에 **GC가 가비지 컬렉팅을 수행할때 네이티브 자원을 회수해 줄 것을 기대할 수 있다.**

finalizer를 사용하면 public API에 나타나기 때문에 알 수 있지만 Cleaner를 사용할지 안할지의 여부는<br>
내부 구현 방식에 관한 문제이므로 public API에 나타나지 않는다는 특징이 있다.

finalizer는 외부에서 호출할 수 있는 public API에 해당하지만 Cleaner는 내부에서 사용하는 <br>
**private 필드이므로 외부에서 접근이 불가능한 API에 해당되어 finalizer와 차이가 있다.**

NativeResourceCleaner 인스턴스는 절대 바깥의 NativeResource 인스턴스를 참조해서는 안 된다.<br>
**NativeResource 인스턴스를 참조할 경우 순환참조가 생겨 가비지 컬렉터가 NativeResource를** <br>
**회수해갈 기회조차 생기지 않는다.**

정적 클래스가 아닌 중첩 클래스는 자동으로 바깥 객체의 참조를 가지게 되고, 람다 역시 바깥 객체의 참조를 갖기 쉬우니 사용하지 않는 것이 좋다



 

















