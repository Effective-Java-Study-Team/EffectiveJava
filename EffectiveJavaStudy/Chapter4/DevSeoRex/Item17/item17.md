### 아이템 17 - 변경 가능성을 최소화하라

### **왜 클래스를 불변으로 만들려고 할까?**
불변 클래스는 그 인스턴스의 내부 값을 수정할 수 없는 클래스다. <br>
불변 인스턴스에 간직된 정보는 고정되어 객체가 파괴되는 순간까지 절대 변하지 않는다.

불변 클래스는 가변 클래스보다 훨씬 안전하고 사용하기 쉬우며, 오류가 생길 여지도 적기 때문에 훨씬 안전하다.

### **클래스를 불변으로 만드는 법**

- **객체의 상태를 변경하는 메서드(변경자)를 제공하지 않는다.**
    - 무분별한 변경자 사용은 값이 변경된 이력 추적을 어렵게 한다.
    - 불변 클래스는 내부 값을 수정할 수 없기 때문에 필드에 대한 접근도 막아야 하지만 변경자를 제공하지 않아야 한다.
- **클래스를 확장할 수 없도록 해야한다.**
    - 하위 클래스에서 부주의하게 또는 나쁜 의도로 객체의 상태를 변하게 만드는 사태를 막아준다.
    - 상속을 막으려면 클래스를 final로 만드는 방법도 있지만, 기본 생성자를 private으로 지정하고 정적 팩터리 메서드를 제공하는 방법이 있다.
- **모든 필드를 final로 선언한다.**
    - 시스템이 강제하는 수단을 이용해 설계자의 의도를 명확히 드러내는 방법이다.
    - 마치 추상 클래스에 abstract 키워드를 붙이는 것 처럼 이 클래스는 인스턴스 생성용 클래스가 아니라는 것을 명시하는 것 처럼 말이다.
    - 새로 생성된 인스턴스를 동기화 없이 다른 스레드로 건네도 문제없이 동작하게끔 보장하는 데도 필요하다.
- **모든 필드를 private으로 선언한다.**
    - 필드가 참조하는 가변 객체를 직접 접근해 수정하는 것을 막아준다.
    - 불변 객체를 참조하는 필드를 public final로만 선언해도 불변 객체가 된다.
        - 하지만 다음 릴리즈를 생각하면 내부 표현을 바꿀 수 없다는 문제가 있다.
- **자신 외에는 내부의 가변 컴포넌트에 접근할 수 없도록 한다.**
    - 클래스에 가변 객체를 참조하는 필드가 하나라도 있다면 클라이언트에서 그 참조를 절대 얻을 수 없도록 해야 한다.
    - 접근자 메서드를 제공하지 않는 것이 좋겠지만, 만약 제공할 필요성이 있다면 반드시 방어적 복사를 수행해야 한다.

### 불변 클래스 살펴보기

```java
public final class Complex {

    private final double re;
    private final double im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double realPart() { return re; }
    public double imaginaryPart() { return im; }

    public Complex plus(Complex c) {
        return new Complex(re + c.re, im + c.im);
    }

    public Complex minus(Complex c) {
        return new Complex(re - c.re, im - c.im);
    }

    public Complex times(Complex c) {
        return new Complex(re * c.re - im * c.im,
                               re * c.im + im * c.re);
    }

    public Complex divideBy(Complex c) {
        double tmp = c.re * c.re + c.im * c.im;

        return new Complex((re * c.re + im * c.im) / tmp,
                            (im * c.re - re * c.im) / tmp );
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Complex)) return false;

        Complex c = (Complex) o;

        return Double.compare(c.re, re) == 0 && Double.compare(c.im, im) == 0;
    }

    @Override
    public int hashCode() {
        return 31 * Double.hashCode(re) + Double.hashCode(im);
    }

    @Override
    public String toString() {
        return "(" + re + " + " + im + "i)";
    }

}
```

복소수를 표현하는 Complex 클래스는 불변 클래스로 설계된 클래스다. <br>
사칙연산 메서드들이 인스턴스 자신의 상태는 수정하지 않고 새로운 Complex 인스턴스를 만들어 반환한다.

피연산자에 함수를 적용해 결과는 반환하지만, 피연산자 자체는 그대로인 프로그래밍 패러다임을 함수형 프로그램이라고 한다. 함수형 프로그래밍은 순수함수라는 개념이 있는데 순수함수는 **Side-Effect를 주지 않는다는 것이다.**

절차적 또는 명령형 프로그래밍에서는 메서드에서 피연산자인 자신을 수정해 자신의 상태가 변한다. <br>
메서드 이름을 **동사(add와 같은) 대신 전치사(plus 같은)**를 사용한 점에도 주목해야한다.

해당 메서드가 객체의 값을 변경하지 않는다는 사실을 강조하기 위해 전치사를 사용하고 있는것이다. <br>
이 명명규칙을 따르지 않은 BigInteger와 BigDecimal 클래스를 사람들이 잘못 사용해 오류가 발생하는 일이 있다고 한다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/ee01f6c5-4b15-483d-b29c-39ad0b752a74)

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/9298e988-4491-40dd-971e-b4b9e70371db)

사용자는 add를 보고 객체의 상태가 바뀐다고 생각할텐데 내부 구현을 보면 새로운 인스턴스를 만들어 반환하고 있기 때문이다. 

```java
BigDecimal bigDecimal = new BigDecimal(10);
BigDecimal bigDecimal2 = bigDecimal.add(BigDecimal.valueOf(10));

BigInteger bigInteger = new BigInteger("10");
BigInteger bigInteger2 = bigInteger.add(BigInteger.valueOf(20));

System.out.println(bigDecimal.equals(bigDecimal2)); // false
System.out.println(bigDecimal.compareTo(bigDecimal2)); // -1
System.out.println((bigDecimal == bigDecimal2)); // false

System.out.println(bigInteger.equals(bigInteger2)); // false
System.out.println(bigInteger.compareTo(bigInteger2)); // -1
System.out.println((bigInteger == bigInteger2)); // false
```

add 메서드를 호출하고 반환받은 인스턴스는 새로운 인스턴스이기 때문에 어떤 비교 방법을 쓰더라도 <br>
결국 다른 인스턴스라는 결과가 나온다.

### 불변 객체의 장점과 활용

불변 객체는 단순하다. 불변 객체는 생성된 시점의 상태를 파괴될 때까지 유지한다. <br>
모든 생성자가 클래스 불변식을 보장한다면 그 클래스를 사용하는 프로그래머는 다른 노력을 들이지 않고도 영원한 불변 객체로 사용할 수 있다.

모든 생성자가 불변식을 보장해야 한다는 의미는, 객체가 생성되고 나서도 그 객체의 **내부 상태가 변하지 않는 것을 보장**하기 위해 **생성자들이 적절한 방식으로 구현되어야 한다는 의미다.** 

그에 반해 가변 객체는 임의의 복잡한 상태에 놓일 수 있다. <br>
변경자 메서드가 일으키는 상태 전이를 정밀하게 문서로 남겨두지 않은 가변 클래스는 믿고 사용하기 어렵다.

**불변 객체는 근본적으로 Thread-Safe 하기 동기화가 따로 필요하지 않다.** <br>
불변 객체는 안심하고 공유할 수 있다. 따라서 불변 클래스라면 한번 만든 인스턴스는 최대한 재활용하면 좋다.

가장 좋은 재활용 방법은 자주 쓰이는 값들을 상수로 제공하는 것이다.

불변 클래스는 자주 사용하는 인스턴스를 캐싱하여 같은 인스턴스를 중복 생성하지 않을 수 있다. <br>
정적 팩터리를 사용하면 여러 클라이언트가 인스턴스를 공유하기 때문에 메모리 사용량이 줄게된다.

새로운 클래스를 설계할때 public 생성자 대신 정적 팩터리를 만들어두면, **클라이언트를 수정하지 않고도 필요에 따라 캐시 기능을 나중에 덧붙일 수 있는 것이다.**

```java
class Apple {

    public Apple() {}
}

// public 생성자 방식
Apple apple = new Apple();

class Apple {

    private Apple() {}
    
    public static Apple getInstance() {
    	return new Apple();
    }
}

// 정적 팩터리 방식 -> 캐시 기능을 덧붙여도 클라이언트 코드의 변화는 없다.
Apple apple = Apple.getInstance();

class Apple {

    private static Apple APPLE = new Apple();
    
    private Apple() {}
    
    // 캐싱 기능 도입
    public static Apple getInstance(int price) {
    	return APPLE;
    }
}

// 캐싱 기능을 붙여도 클라이언트 코드는 변하지 않는다.
Apple apple = Apple.getInstance();

```

불변 객체를 자유롭게 공유할 수 있다는 점은 방어적 복사도 필요 없다는 결론이 나오게된다. <br>
아무리 복사해봐야 원본과 매번 같기 때문에 clone 메서드나 복사 생성자를 제공하지 않는게 좋다.

불변 객체끼리는 내부 데이터를 공유할 수 있다. <br>
BigInteger를 예로 들면 내부의 값의 부호(sign)와 크기(magnitude)를 따로 표현하고 있다.

BigInteger 클래스의 negate 메서드는 크기를 나타내는 mag 배열을 새로운 BigInteger 객체를 생성할때 사용하여 공유하고 있다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/dbea3618-dd19-4251-b5ea-983ab59ee13c)

mag 배열은 가변이지만, 외부에서 접근이 불가능하고 BigInteger 내부 동작을 위해 공유하기 때문에 변경에 대해서 안전하다.

또, 새로 생성한 BigInteger 객체는 부호가 다르다는 것만 빼고는 숫자나 소수점 자리수마저 같다. <br>
따라서 **mag 배열을 공유해서 쓰면 캐싱효과가 나게되어 메모리 절약도 할 수 있는 것이다.**

불변 객체는 Map의 key와 Set의 원소로 쓰기 아주 좋다. <br>
Map이나 Set은 안에 담긴 값이 바뀌면 불변식이 허물어지는 일이 발생하기 때문이다.

Set은 중복을 허용하지 않는 컬렉션인데, 외부의 레퍼런스를 이용해 Set 내부의 원소의 값을 변경해서 <br>
다른 원소와 같아진 다면 어떻게 될까?

Map은 키에 해당하는 객체의 상태가 변경되면 객체로부터 계산된 해시 코드가 변경될 수 있으므로, <br>
다음에 그 객체를 이용해서 값을 찾을때 문제가 발생할 수 있다.

```java
WrapperInteger setElement1 = new WrapperInteger(10);
WrapperInteger setElement2 = new WrapperInteger(20);

Set<WrapperInteger> set = new HashSet<>();

set.add(setElement1);
set.add(setElement2);

// set = [WrapperInteger{number=10}, WrapperInteger{number=20}]
System.out.println("set = " + set); 
setElement2.setNumber(10);

// 값을 바꿔서 Set 내부의 원소에 중복을 허용하게 되는 문제가 발생한다.
// set = [WrapperInteger{number=10}, WrapperInteger{number=10}]
System.out.println("set = " + set);

WrapperInteger mapKey = new WrapperInteger(10);
Map<WrapperInteger, Integer> map = new HashMap<>();
map.put(mapKey, 10);
System.out.println(map.get(mapKey)); // 10

// Map의 Key 값을 변경해서 원소를 찾지 못하는 문제가 발생한다.
mapKey.setNumber(20);
System.out.println(map.get(mapKey)); // null
```

불변 객체는  그 자체로 실패 원자성을 제공하는 효과도 있다. <br>
실패 원자성이란 메서드에서 **예외가 발생한 후에도 그 객체는 여전히 유효한 상태**여야 한다는 것이다.

여기서 유효하다는 의미는 **메서드 호출 전과 후의 객체 상태가 같아야 한다는 뜻이다.** <br>
불변 객체의 메서드는 내부 상태를 바꾸지 않을 것이기 때문에 그렇다.

### 불변 객체의 단점

불변 객체가 장점이 더 많아서 사용하고 있지만 단점이 없는 것은 아니다.

불변 클래스는 값이 다르면 반드시 독립된 객체로 만들어야 한다는 것이다. <br>
값의 가짓수가 많은 경우 하나만 변경하려고 해도 이들들 모두 만드는 큰 비용을 치러야한다.

```java
BigInteger moby = ...; // 100만 비트짜리 숫자
moby = moby.flipBit(0); // 한자리의 비트만 바꾸는데 100만 비트의 인스턴스를 다시 만든다.

BitSet moby = ...; // 100만 비트짜리 숫자
moby.flip(0); // 원하는 비트 하나만 상수 시간 안에 바꿔주는 메서드를 제공한다.
```

불변이라서 무조건 좋고, 가변이라서 무조건 나쁜 것이 아니다. <br>
이렇게 성능과 비용에 따라서 불변 객체를 사용하지 않아야 할 경우도 생기는 것이다.

특히 원하는 객체를 완성하기까지의 단계가 많고, 그 중간 단계에서 만들어진 객체들이 모두 버려진다면 성능 문제는 더 불거질 것이다.

이 문제를 어떻게 해결해야 할까?

- **다단계  연산들을 예측하여 기본 기능으로 제공한다.**
    - BigInteger 클래스는 모듈러 지수 같은 **다단계 연산 속도롤 높여주는 가변 동반 클래스**를 package-private으로 두고 있다.
    - 클라이언트들이 원하는 복잡한 연산이 무엇인지 정확히 예측할 수 있다면, package-private의 가변 동반 클래스만으로 충분히 대응할 수 있다.
- **다단계 연산들을 예측할 수 없다면 public으로 제공해야 한다.**
    - String 클래스가 대표적인 예시다. StringBuiler와 StringBuffer를 가변 동반 클래스로 제공한다.

불변 클래스를 만드는 가장 기본적인 방법은 final로 선언해서 상속하지 못하게 하는 방법이 있다. <br>
더 유연한 방법은 **모든 생성자를 private 혹은 package-private**으로 선언하고 정적 팩터리 메서드를 만들어두는 것이다.

**대부분 이 방식이 최선인 경우가 많다.**

package-private 구현 클래스를 원하는 만큼 만들어 활용할 수 있으니 훨씬 유연한 것이다. <br>
public 또는 protected 레벨의 생성자가 없기 때문에 **다른 패키지에서는 이 클래스를 확장하는 게 불가능하기 때문이다.**

신뢰할 수 없는 하위 클래스의 인스턴스라고 확인되면, 이 인수들은 가변이라 가정하고 방어적으로 복사해 사용해야 한다.

BigInteger와 BigDecimal 클래스의 모든 메서드는 재정의 가능하도록 설계되었기 때문에 이 인스턴스가 진짜 **BigInteger 또는 BigDecimal인지 확인해야 할 필요가 생긴것이다.**

불변 클래스의 어떤 메서드도 그 객체를 수정할 수 없어야 한다는 규칙은 너무 과한 면이 있다. <br>
**따라서 어떤 메서드도 객체의 상태 중 외부에 비치는 값을 변경할 수 없다. 로 재정의 하는 것이 맞아보인다.**

어떤 불변 객체는 계산 비용이 큰 값을 나중에 계산하여 final이 아닌 필드에 캐시해두기도 한다. <br>
순전히 그 객체가 불변이기 때문에 사용할 수 있는 방법이다.

### 불변 객체를 만들거나 설계할때의 주의사항

- **Getter가 있다고 해서 무조건 Setter를 만들면 안된다.**
    - 클래스는 꼭 필요한 경우가 아니라면 불변이여야 한다.
    - 단순한 값 객체는 항상 불변으로 만들어야 한다.
        - 자바 플랫폼에서도 몇개의 아픈 손가락들이 있다(Point, Date)
- **성능 때문에 어쩔 수 없다면 가변 동반 클래스를 public 클래스로 제공하자**
- **불변으로 만들 수 없는 클래스라도 변경할 수 있는 부분을 최소한으로 줄여야한다.**
- **합당한 이유가 없다면 모든 필드는 private final 이어야한다.**
- **생성자는 불변식 설정이 모두 완료되어 초기화가 완벽히 끝난 상태의 객체를 생성해야 한다.**
    - 생성자와 정적 팩터리 외에는 그 어떤 초기화 메서드도 public으로 제공해서는 안된다.

java.util.concurrent 패키지의 CountDownLatch 클래스가 위 원칙들을 잘 지키고 있다. <br>
비록 가변 클래스지만 가질 수 있는 상태의 수가 많지 않다.

인스턴스를 생성해 한 번 사용하고 **카운트가 0에 도달하면 더는 재사용이 안된다.**
