### 아이템 19 - 상속을 고려해 설계하고 문서화하라. 그러지 않았다면 상속을 금지하라

상속을 고려한 설계와 문서화란 정확히 무엇을 뜻하는 말일까?

상속용 클래스는 재정의할 수 있는 메서드들을 내부적으로 어떻게 이용하는지 문서로 남겨야 한다. <br>
어떤 순서로 호출하는지, 호출 결과가 이어지는 처리에 어떤 영향을 주는지도 담아야 한다.

여기서 재정의 가능한 메서드란, **public과 protected 메서드 중 final이 아닌 모든 메서드**를 뜻한다.

API 문서의 메서드 설명 끝 부분에 “**Implementation Requirements**”로 시작하는 문구가 있다.  <br>
그 메서드의 내부 동작 방식을 설명하는 부분이다. 이 메서드가 재정의 가능하기 때문이다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/576c060a-820d-44c3-960d-842c094ed993)

이 주석은 메서드 주석에 @implSpec 태그를 붙이면 자바독 도구가 생성해준다.

**implementation Requirements**의 설명에 따르면, iterator 메서드를 재정의하면 remove 메서드의 동작에 영향을 주는 것을 알 수 있다.

iterator 메서드로 얻은 반복자의 동작이 **remove 메서드의 동작에 영향**을 주는 것도 정확히 설명하고 있다. <br>
HashSet을 상속해서 **add를 재정의한 것이 addAll에 영향**을 준다는 사실을 알지 못했던 때와 아주 대조적이라고 볼 수 있다.

상속은 캡슐화를 해치기 때문에, 클래스를 안전하게 상속할 수 있도록 내부 구현 방식을 설명해야만 한다.

### 문서화 이외에도 고려해야 할 것들

내부 구현을 문서화 하는 것만으로 상속을 위한 설계를 다했다고 볼 수 없다. <br>
**클래스의 내부 동작 과정 중간에 끼어들 수 있는 hook**을 잘 선별해서 protected 메서드 형태로 공개하는 것도 염두해야한다. protected 필드로 특정 필드를 공개해야 하는 경우도 있다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/0d3ad471-f03f-48e5-b834-3a6b1495df08)

AbstractList의 removeRange 메서드가 그 예로 아주 적합하다. <br>
List 구현체를 사용하는 클라이언트 입장에서는 removeRange 메서드의 내부 구현에 대해 관심이 없다.

그럼에도 이 메서드에 대한 내부 구현 정보를 제공하는 이유는 하위 클래스에서 **부분 리스트의 clear 메서드를 고성능으로 만들기 쉽게 하기 위해서다.**

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/0cb3e739-24cb-469a-b7d1-1d5668e44bfc)

AbstractList의 clear 메서드는 단순히 removeRange를 내부호출하고 있다. <br>
따라서 **removeRange 메서드가 없다면, 하위 클래스에서 부분리스트의 메커니즘을 밑바닥부터 새로 구현해야 하거나 성능이 크게 떨어지는 문제가 발생했을 것이다.**

상속용 클래스를 설계할 때 **어떤 메서드를 protected로 노출해야 할지**는 어떻게 결정해야 할까? <br>
하위 클래스를 실제로 만들어보고 시험해보는 방법이 가장 최선이다.

**protected 메서드는 하나하나가 내부 구현**에 해당하므로 그 수는 가능한 적어야 한다. <br>
하지만 **너무 적게 노출해서 상속으로 얻는 이점마저 없애는 일은 없도록 주의**해서 클래스를 설계해야 한다.

하위 클래스를 직접 만들어보며 검증해보면, <br>
꼭 필요한 protected 멤버를 놓쳤다면 **하위 클래스를 작성할 때 분명히 빈자리**가 드러난다. <br>
반대로 **하위 클래스를 여러 개 만들 때까지 전혀 쓰지 않는 protected 멤버가 존재**한다면 이는 private 이었어야 할 가능성이 크다.

따라서 상속용으로 설계한 클래스는 **배포 전에 반드시 하위 클래스를 만들어 검증해야 한다.**

### 상속할때 클래스가 지켜야 하는 제약 몇가지

- **상속용 클래스의 생성자는 재정의 가능 메서드를 호출해서는 안된다.**

```java
public class Super {

    public Super() {
        // 상속용 클래스는 재정의 가능한 메서드를 생성자내에서 호출해서는 안된다.
        overrideMe();
    }

    public void overrideMe() {
    }

}

public class Sub extends Super {

    private final Instant instant;

    Sub() {
        super();
        instant = Instant.now();
    }

    @Override
    public void overrideMe() {
        System.out.println(instant);
    }

}
```

이 코드에서 드러나는 문제점은, 상위 클래스의 생성자가 하위 클래스의 생성자보다 먼저 호출된다는 점인데, <br>
재정의한 메서드가 **하위 클래스의 생성자에서 초기화하는 값에 의존**하고 있기 때문에 재대로 동작하지 못한다.

```java
Sub sub = new Sub();
sub.overrideMe(); // null 출력 후 Instant 객체의 시간을 보여준다.
```

단순 출력을 하는 println 메서드를 호출했기 때문에 아무 문제가 발생하지 않았지만, <br>
다른 메서드의 인자로 넘겼다면 **NullPointerException이 발생**했을 수 있다.

- **Cloneable과 Serializable 인터페이스는 상속용 설계의 어려움을 더한다.**

두 인터페이스 중 하나라도 구현한 클래스를 상속할 수 있도록 설계하는 것은 일반적으로 매우 좋지 않다. 프로그래머에게 엄청난 부담을 지우기 때문이다.

clone과 readObject 메서드는 생성자와 비슷한 효과를 내게된다. <br>
**clone과 readObject** 역시 재정의 가능한 메서드를 내부에서 호출해서는 안된다.

```java
public class A implements Cloneable {

    int x;
    int y;

    public A(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public A clone() {
        try {
            A clone = (A) super.clone();
            overrideMe();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void overrideMe() {}

}

public class B extends A{

    int j;

    public B(int x, int y, int j) {
        super(x, y);
        this.j = j;
    }

    @Override
    public B clone() {
        return (B) super.clone();
    }

    @Override
    public void overrideMe() {
        super.x  = 10;
    }
}
```

클래스 A의 clone 메서드가 내부적으로 overrideMe를 호출하고 있다.

clone도 생성자 호출처럼 연쇄 호출을 하게 되는데, 그 과정에서 **B가 오버라이딩한 overrideMe가 호출되게 되면서 부모의 필드인 x 값이 10으로 변경되어 객체가 손상**되게 된다.

따라서 복사하고자 하는 B의 상태와 다른 객체가 clone되게 되는 것이다.

```java
B b1 = new B(30, 40, 50);
B b2 = b1.clone();

System.out.println("b1 = " + b1);  //  b1 = B{j=50, x=30, y=40}
System.out.println("b2 = " + b2);  //  b2 = B{j=50, x=10, y=40}
```

객체 직렬화에도 이런 문제는 동일하게 발생한다.

```java
public class C implements Serializable {

    int x;
    int y;

    public C(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        inputStream.defaultReadObject();
        overrideMe();
    }

    public void overrideMe() {}
}

public class D extends C {

    int z;

    public D(int x, int y, int z) {
        super(x, y);
        this.z = z;
    }

    @Override
    public void overrideMe() {
        super.x = 0;
    }

    @Override
    public String toString() {
        return "D{" +
                "z=" + z +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
```

D를 직렬화하고, 다시 역직렬화 했을때 반환한 D 객체의 x 값이 오버라이딩 한 메서드로 인해서 0이 된다. <br>
부모인 C 클래스에서 overrideMe 메서드를 내부 호출하고 있기 때문이다.

```java
D d = new D(30, 40, 50);

byte[] serializedD = serialize(d);
D deserializedD = (D) deserialize(serializedD);

System.out.println(d);  // D{z=50, x=30, y=40}
System.out.println(deserializedD); // D{z=50, x=0, y=40}
```

- **Clone에서 추가적으로 주의할 점**

특히 clone은 하위 클래스의 clone 메서드가 복제본의 상태를 수정하기 전에 재정의한 메서드를 호출하는 문제가 있다.

특히 clone이 잘못되면 원본 객체까지도 피해를 볼 수 있는데, 원본 깊숙한 내부 자료구조까지 복사했다고 생각했는데 **clone을 완벽하게 정의하지 않아서 복제본의 내부 어딘가 원본 객체의 데이터를 참조하고 있을 수 있기 때문이다.**

- **Serializable 구현한 클래스 상속시 추가 주의 사항**

**readResolve 또는 writeReplace** 메서드를 갖는다면 이 메서드들은 private가 아닌 protected로 선언해야 한다. private로 선언하면 하위 클래스에서 무시되기 때문이다.

### 정리

상속용으로 설계하지 않은 클래스는 상속을 금지해야 한다. <br>
상속을 금지하는 방법으로는, **클래스를 final로 선언**하거나 **모든 생성자를 private 또는 package-private로 선언**하고 public 정적 팩터리를 만들어주는 방법이다.

래퍼 클래스 패턴 역시 기능을 확장할 때 상속 대신 쓸 수 있는 더 나은 대안이기도 하다. <br>
클래스 내부에서 재정의 가능 메서드를 사용하지 않고, 이 사실을 문서로 남긴다면 상속을 허용해도 문제가 생기지 않는다.

이렇게 하면 메서드를 재정의해도 다른 메서드의 동작에 아무런 영향을 주지 않는다. <br>
물론 부모 클래스의 메서드를 사용하거나 의존적으로 이용하고 있다면 다음 릴리즈때 부모 클래스의 내부 구현 변동에 따라 문제가 생길 소지는 있다.

클래스 동작은 유지하면서 재정의 가능 메서드를 사용하는 코드의 제거방법 중 가장 간단한 방법이있다. <br>
재정의 가능한 메서드를 내부에서 사용하고 있다면, 그 코드를 private 메서드로 옮기는 것이다.

### 정리
    
1. 클래스 내부에서 스스로를 어떻게 사용하는지 모두 문서로 남겨야한다.
2. 문서화 한 것은 반드시 지켜야 한다.
3. 효율 좋은 하위 클래스를 만들 수 있도록 일부 메서드를 protected로 제공해야 할 수도 있다.
4. 클래스를 확장해야 할 명확한 이유가 없다면 상속을 금지하는 편이 낫다.
5. 상속을 금지하려면 클래스를 final로 선언하거나 생성자 모두를 외부에서 접근할 수 없도록 만들면 된다.
