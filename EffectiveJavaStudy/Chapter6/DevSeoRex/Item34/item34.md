### 아이템 34 - int 상수 대신 열거 타입을 사용하라

### 상수 열거 타입을 사용하면 안되는 이유

```java
public class IntEnumPattern {

    private IntEnumPattern() {}

    public static final int APPLE_FUJI = 0;
    public static final int APPLE_PIPPIN = 1;
    public static final int APPLE_GRANNY_SMITH = 2;

    public static final int ORANGE_NAVEL = 0;
    public static final int ORANGE_TEMPLE = 1;
    public static final int ORANGE_BLOOD = 2;
}
```

정수 상수를 한 묶음으로 모아서 유틸리티로 지원하는 클래스를 `정수 열거 패턴 기법`을 활용한 클래스라고 한다. <br>
이 메서드는 `타입 안전`을 보장할 방법이 없고 표현력도 좋지 않다.

만약 `사과`가 필요한 메서드에 `오렌지`에 해당하는 상수를 넣는다고 해도 아무런 문제가 생기지 않을 것이다.

```java
public static void main(String[] args) {
    // 향긋한 오렌지 향의 사과 소스! -> 오렌지를 건네야 할 메서드에 사과를 보내도 경고가 없다!
    int i = makeSauce(APPLE_FUJI , ORANGE_TEMPLE, APPLE_PIPPIN);
}
    
    
public static int makeSauce(int apple1, int apple2, int apple3) {
    return (apple1 + apple2) / apple3;
}
```

자바는 `정수 열거 패턴`을 위한 별도 `이름공간`을 지원하지 않기 때문에 상수의 이름을 지을때 보통 접두어를 써서 이름 충돌을 방지하곤 한다.

- **이름공간(namespace)**
    `이름공간`이란이란 이름을 구분 할 수 있게 해주는 공간을 의미한다. `자바`에서는 패키지를 공간으로 쓰고 있다. <br>
    같은 패키지 안에서는 같은 이름의 클래스가 존재할 수 없지만 패키지가 다르다면 얼마든지 가능하다.
    

`정수 열거 패턴`을 사용하면 생기는 문제로는 상수를 나열한 클래스를 활용하면 컴파일시에 그 값을 사용한 클라이언트 파일에 `그대로 새겨진다.` <br>
상수의 값이 바뀌면 클라이언트도 반드시 `다시 컴파일`해야 한다는 `문제`가 있다.

`정수 상수`는 문자열로 출력하기가 까다롭다는 것도 문제의 하나다. <br>
값을 출력해도 `의미`가 아닌 `숫자`로만 나오니 어떤 의미를 가진 것인지 알기가 힘들다.

클래스 안에 몇 개의 상수가 있는지, 그걸 다 `순회`할 수 있는 방법 같은 것도 없어서 이 부분도 필요하다면 꽤나 곤란한 상황을 연출시킨다. <br>
`정수 열거 패턴`과 비슷한 예시로 `문자열 상수`를 사용하는 `문자열 열거  패턴`도 있지만 이건 더 나쁘다.

`컴파일 타임`에 오타를 잡아낼 수 없고, 문자열 비교로 인한 성능 저하는 문제에 문제를 더하는 격이다.

### 열거 타입의 등장

위에서 나열했던 `문자열` 또는 `정수`를 이용한 `열거 패턴`들의 문제를 해결할 방법이 생겼다. <br>
바로 `Enum`을 이용한 방법이다.

```java
public enum Apple { FUJI, PIPPIN, GRANNY_SMITH }
public enum Orange {NAVEL, TEMPLE, BLOOD}

public static int makeSauce(Apple apple1, Apple apple2, Apple apple3) {
    return 0;
}

// 메서드 'makeSauce(Apple, Orange, int)'를 해결할 수 없습니다
int j = makeSauce(Apple.FUJI, Orange.TEMPLE, APPLE_PIPPIN);
```

이 문제는 `Enum`을 사용하는 것으로 해결할 수 있다. <br>
`Apple`이 필요한 곳에 `Orange`를 넣을 수 없다. 열거 타입은 단순 상수가 아니라 클래스이기 때문이다.

열거 타입은 상수 하나당 `자신의 인스턴스`를 하나씩 만들어 `public static final` 필드로 공개한다. <br>
열거 타입은 밖에서 접근할 수 있는 `생성자`를 제공하지 않으므로 `인스턴스`를 만들 수 없다.

`열거 타입`은 인스턴스 통제되고, `싱글턴` 이라고 볼 수 있다. <br>
`열거 타입`은 단 하나의 `인스턴스`만 존재하고 있음을 보장받을 수 있기 때문이다.

열거 타입은 `상수를 추가`하거나 `순서를 바꿔`도 다시 `컴파일`하지 않아도 된다. <br>
`정수 열거 패턴`과 달리 상수 값이 `컴파일 타임`에 각인되지 않기 때문이다.

`열거 타입`에는 `메서드`나 `필드`를 추가할 수 있고, `인터페이스 구현`도 가능하다. <br>
`Object 메서드`들도 높은 품질로 구현되었을 뿐만 아니라 `Comparable`과 `Serializable`도 구현되어 있다.

```java
public enum Planet {

    MERCURY(3.302e+23, 2.439e6),
    VENUS(4.869e+24, 6.052e6),
    EARTH(5.5976e+24, 6.378e6);

    private final double mass; // 질량
    private final double radius; // 반지름
    private final double surfaceGravity; // 표면중력

    private static final double G = 6.67300E-11;

    Planet(double mass, double radius) {
        this.mass = mass;
        this.radius = radius;
        this.surfaceGravity = G * mass / (radius * radius);
    }

    public double mass() { return mass; }
    public double radius() { return radius; }
    public double surfaceGravity() { return surfaceGravity; }

    public double surfaceWeight(double mass) {
        return mass * surfaceGravity; // F = ma
    }
}
```

`열거 타입`은 `불변`이기 때문에 모든 필드가 `final`이어야 한다. 따라서 `생성자`에서 전부 초기화 해주면 된다. <br>
`상수 열거 패턴`과 다르게, `Enum`은 모든 값을 순회할 수 있기 때문에 `지구`에서의 무게를 입력 받아서 다른 행성들과의 무게를 출력하는 것을 `적은 양의 코드`로 끝낼 수 있다.

```java
double earthWeight = 180;
double mass = earthWeight / Planet.EARTH.surfaceGravity();
for (Planet p : Planet.values()) {
		System.out.printf("%s에서의 무게는 %f이다. %n", p, 
          p.surfaceWeight(mass));
    /*
    * 출력
    * MERCURY에서의 무게는 72.609449이다. 
    * VENUS에서의 무게는 173.892804이다. 
    * EARTH에서의 무게는 180.000000이다.
    */
}
```

만약 `열거 타입`의 `상수`를 하나 `제거`하면 어떻게 될까? <br>
`무게`를 출력하는 위와 같은 `프로그램`에서는 출력이 하나 줄어들 뿐 큰 영향은 없다.

만약 해당 상수를 직접 참조하는 `클라이언트`가 있다면 그 `클라이언트 프로그램`에서만 `컴파일 오류`가 발생한다.

### 열거타입을 다루는 기술

열거 타입을 만들때는 고려할 부분이 많다. 어떤 것들이 있을까?

- **열거 타입을 선언한 클래스 혹은 그 패키지에서만 유용한 기능이 있는가?**

만약 있다면 `private` 또는 `package-private` 메서드로 구현해야 한다. <br>
`일반 클래스`와 같이 그 기능을 클라이언트에게 노출해야 할 합당한 이유가 없다면 `protected` 이하의  `접근 제어`를 가져갈 필요가 없다.

널리 쓰이는 `열거 타입`은 `톱레벨 클래스`로 만들어야 한다. <br>
만약 `특정 클래스`에서만 쓰인다면 `멤버 클래스`로 만드는 것이 좋다.

- **상수마다 다른 동작을 해야한다면 어떻게 해야할까?**

```java
public enum Operation {

    PLUS, MINUS, TIMES, DIVIDE;

    public double apply(double x, double y) {
        switch (this) {
            case PLUS : return x + y;
            case MINUS : return x - y;
            case TIMES : return x * y;
            case DIVIDE : return x / y;
        }
        throw new AssertionError("알 수 없는 연산 : " + this);
    }
}
```

사칙 연산을 수행하는 계산기를 표현한 열거 타입을 있다. <br>
`switch` 문에 따라서 분기를 통해 연산을 수행하도록 `메서드`를 구현하였다.

만약 `새로운 연산`이 추가된다면, 그 연산에 해당하는 상수를 만들고 `case 문`에 추가하지 않으면 여지 없이 에러를 발생시킬 것이다. <br>
`열거 타입`은 `상수별 메서드 구현`을 가질 수 있다.

따라서 `특정 상수`에 `클래스 몸체`를 만들고 그 안에서 `메서드를 구현`하면 훨씬 안전하게 `상수별로 다른 동작`을 정의할 수 있다.

```java
public enum Operation {

    PLUS {
        public double apply(double x, double y) {
            return x + y;
        }
    },
    MINUS {
        public double apply(double x, double y) {
            return x - y;
        }
    },
    TIMES {
        public double apply(double x, double y) {
            return x * y;
        }
    },
    DIVIDE {
        public double apply(double x, double y) {
            return x / y;
        }
    };

    public abstract double apply(double x, double y);
}
```

`apply` 메서드를 `추상 메서드`로 선언함으로서, 구현을 강제하기 때문에 프로그래머의 실수로 메서드 구현을 누락하지 않도록 `안전하게 설계`했다.

```java
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

    OperationRefactor(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return this.symbol;
    }

    public abstract double apply(double x, double y);
}
```

`연산에 해당하는 기호`를 문자열로 `상수별`로 가지게 있게 함으로서, `연산 기호`도 반환할 수 있도록 클래스를 <br>
`리팩터링`했다. 이렇게 되면 계산하는 과정을 기호로 편히 볼 수 있다.

```java
for (Operation op : Operation.values()) {
		System.out.printf("%f %s %f = %f%n", 
            2.0, op, 4.0, op.apply(2.0, 4.0));
}

// 출력
2.000000 + 4.000000 = 6.000000
2.000000 - 4.000000 = -2.000000
2.000000 * 4.000000 = 8.000000
2.000000 / 4.000000 = 0.500000
```

매번 `apply` 메서드를 직접 쓰는 것도 코드 양이 상당하다. <br>
만약 이 부분을 `함수형 인터페이스`를 이용해 해결하면 간단하게 코드를 작성할 수 있을 것이다.

```java
public enum OperationFinalRefactor {

    PLUS("+", (x, y) -> x + y),
    MINUS("-", (x, y) -> x - y),
    TIMES("*", (x, y) -> x * y),
    DIVIDE("/", (x, y) -> x / y);

    public final DoubleBinaryOperator operator;
    private final String symbol;

    OperationFinalRefactor(String symbol, DoubleBinaryOperator operator) {
        this.symbol = symbol;
        this.operator = operator;
    }

    @Override
    public String toString() {
        return this.symbol;
    }
}
```

열거 타입에서 `toString`을 구현할 경우 `toString`이 반환하는 문자로 열거 타입을 찾아주는 `메서드`를 정의할지 고려해 볼 수 있다.

```java
private static final Map<String, OperationRefactor> stringToEnum =
            Stream.of(values()).collect(Collectors.toMap(Object::toString, e -> e));

public static Optional<OperationRefactor> fromString(String symbol) {
		return Optional.ofNullable(stringToEnum.get(symbol));
}
```

- **열거 타입의 생성자**
    `열거 타입 생성자`가 실행되는 시점에는 `자기 자신을 추가하지 못하는 제약`이 꼭 필요하다. <br>
    따라서 같은 `열거 타입`의 `다른 상수`에 대한 `접근`이나, `열거 타입 내의 상수들`에 `접근`이 불가능하다. <br>
    접근이 가능한 범위는 오직 `상수 변수` 뿐이다.
    
- **상수별 메서드 구현에서의 문제**

`열거 타입`에서 `상수별 메서드 구현`에는 `열거 타입 상수`끼리 `코드`를 `공유`하기 어렵다는 `문제`가 있다.

```java
public enum PayrollDay {

    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY,
    SATURDAY, SUNDAY;

    private static final int MINUS_PER_SHIFT = 8 * 60;

    int pay(int minutesWorked, int payRate) {
        int basePay = minutesWorked * payRate;
        int overtimePay;
        switch (this) {
            case SATURDAY : case SUNDAY:
                overtimePay = basePay / 2;
                break;
            default:
                overtimePay = minutesWorked <= MINUS_PER_SHIFT ?
                        0 : (minutesWorked - MINUS_PER_SHIFT) * payRate / 2;
        }

        return basePay + overtimePay;
    }
}
```

`주말`과 `주중`은 임금을 산정하는 방식이 다른데 코드를 `pay 메서드`를 `어설프게 공통화`하여 `switch 문`으로 매번 분기를 수행해야 했던 것이다.  <br>
또 `휴가`와 같이 새로운 `열거 타입`이 추가되면 `case 문`에 `반영`해줘야 할 것이다.

그렇지 않으면 `휴가 기간`이나 공휴일에 일한 직원이 `평일에 해당하는 급여`를 받을 것이기 때문이다. <br>
이 문제를 해결하는 방법은 `전략 열거 타입 패턴`을 도입해서 해결할 수 있다.

`PayType` 이라는 `전략 열거 타입`을 `내부 멤버 클래스`로 선언하고 요일마다 `임금 산정 전략`을 지정해주는 것이다.

```java
public enum PayrollDayRefactor {

    MONDAY(WEEKDAY), TUESDAY(WEEKDAY), WEDNESDAY(WEEKDAY), THURSDAY(WEEKDAY), FRIDAY(WEEKDAY),
    SATURDAY(WEEKEND), SUNDAY(WEEKEND);

    private final PayType payType;

    PayrollDayRefactor(PayType payType) {
        this.payType = payType;
    }

    int pay(int minutesWorked, int payRate) {
        return payType.pay(minutesWorked, payRate);
    }

    enum PayType {
        WEEKDAY {
            int overtimePay(int minsWorked, int payRate) {
                return minsWorked <= MINUS_PER_SHIFT ? 0 :
                        (minsWorked - MINUS_PER_SHIFT) * payRate / 2;
            }
        },
        WEEKEND {
            int overtimePay(int minsWorked, int payRate) {
                return minsWorked * payRate / 2;
            }
        };

        abstract int overtimePay(int minsWorked, int payRate);
        private static final int MINUS_PER_SHIFT = 8 * 60;

        int pay(int minsWorked, int payRate) {
            int basePay = minsWorked * payRate;
            return basePay + overtimePay(minsWorked, payRate);
        }
    }
}
```

이 코드를 사용하는 클라이언트 입장에서는 그저 `pay 메서드`를 `열거형 상수`에 접근해 호출하기만 하면 내부 구현은 신경쓰지 않아도 되는 것이다. <br>
만약 `PayType`에 `새로운 상수`들을 추가한다고 해도 `기존 열거타입`은 영향을 받지 않으며, 코드를 수정하지 않고 전략만 교체하면 되기 때문에 `유지보수성도 매우 뛰어나다.`

`switch 문`은 열거 타입의 상수별 동작을 구현하기에는 적합하지 못하다. <br>
만약 기존 열거 타입에 `상수별 동작을 혼합`해 넣어야 한다면 `switch 문`이 `좋은 선택`이 될 수 있다.

```java
    /*
    *  switch 문을 이용한 방법은 기존 열거 타입에 상수별 동작을 혼합해 넣을 때 유용하다.
    *  예를 들어 아래 메서드처럼 각 연산의 반대 연산을 반환하는 메서드가 필요할때는 이 방법이 좋다.
    * */
    public static Operation inverse(Operation op) {
        switch (op) {
            case PLUS : return Operation.MINUS;
            case MINUS : return Operation.PLUS;
            case TIMES : return Operation.DIVIDE;
            case DIVIDE : return Operation.TIMES;

            default : throw new AssertionError("알 수 없는 연산 : " + op);
        }
    }
```

`상수를 초기화` 할때 반대 연산을 내부 필드로 가지고 있는 방법을 생각해볼 수 있겠지만, <br>
자기 자신을 추가할 수 없다는 `열거형 상수`의 제약때문에 이것도 불가능한 이야기일 것이다.

이런 상황에서는 `switch 문`이 꽤나 좋은 선택이라고 볼 수 있다.

- **열거 타입의 성능**

`열거 타입의 성능`은 `정수 상수`와 많이 차이나지 않는다.

`열거 타입`을 `메모리에 올리는 공간`과 `초기화하는 시간` 은 들겠지만 `컴파일 타임`에 문제를 발견할 수 있고 <br>
`타입 안정성`을 보장하며 항상 `싱글톤임을 보장`받는 것에 비하면 적은 비용일 것이다. <br>
`필요한 원소`를 `컴파일 타임`에 다 알 수 있는 `상수 집합`이라면 `무조건 열거 타입` 을 사용하자.

`열거 타입`에 정의된 `상수 개수`가 늘어나더라도 `바이너리 수준`에서 `호환`되도록 설계되어 있다.

- **정리**
1. `열거 타입`은 더 읽기 `쉽고` 안전하고 `강력`하다.
2. 열거 타입이 `상수별로 다른 동작`을 요한다면 `상수별 메서드 구현`을 사용하자.
3. 열거 타입 상수 일부가 `같은 동작을 공유`한다면 `전략 열거 타입 패턴`을 사용하자.
