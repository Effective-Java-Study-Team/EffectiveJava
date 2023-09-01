# 34장 - int 상수 대신 열거 타입을 사용하라

태그: In progress

JDK 5.0 이전에는 상수들을 모아두고 사용하던 

int enum pattern, String enum pattern 들은 다음 단점을 지니고 있다.

1. 타입 안전 보장 (feat. 정적 바인딩)
2. 표현력 ↓ ( 결국엔 `값` 그 자체로 들어가기 때문)
3. 바이너리 호환성이 떨어져 변화에 민감 → 프로그램이 깨지기 쉽다

이런 단점들 때문에 JDK 5.0 이후부터는 상수들을 모아두기 위해

Enum Type 이 나오게 되었다.

<aside>
💡 Enum Type 이란?

일정 개수의 상수 값을 정의한 다음, 그 외의 값은 허용하지 않는 타입

</aside>

# Enum Type 의 특징

1. 컴파일 타임에 타입 안정성을 제공한다.
    
    `Fruit` Enum Type 을 파라미터로 받게 된다면, 
    
    int enum pattern 처럼 값으로 판단을 하는 게 아닌
    
    `타입으로 판단을 하기 때문이다.`
    
2. 열거 타입에는 각자의 이름공간이 있어, 이름이 같은 상수도 존재 가능
    
    ```java
    public enum Enum1{
    	A,B,C
    }
    
    public enum Enum2{
    	A,B,C
    }
    ```
    
    이 형태가 가능하다.
    
3. toString 메서드는 식별이 가능한 결과를 return 해준다.
    
    ```java
    public enum Fruit {
        Apple, Banana, Watermelon;
    }
    
    System.out.println("Fruit.Apple = " + Fruit.Apple); // Apple (문자열)
    System.out.println("Fruit.Apple.toString() = " + Fruit.Apple.toString()); // Apple (문자열)
    ```
    
4. Enum Type 은 엄연히 클래스 이기 때문에, 필드/메서드 추가가 가능하다.
    
    이 특징은 단순히 상수의 나열이 아닌, 상수별 기능 추가가 가능해진다.
    

# Enum Type 에 필드/메서드 를 왜 추가함?

현실 세계에 존재하는 객체를 클래스의 형태로 가져오고,

이때 객체의 특성과 기능을 필드, 메서드로 가져온다.

Enum Type 도 상수의 필드/메서드를 추가함으로써, 

단순한 상수가 아닌, 특성과 기능을 포함하는 고차원 개념의 표현이 가능하다.

ex) 태양계 행성 표시, 등등

- 태양계 행성 Enum Type
    
    ```java
    public enum Planet {
        MERCURY(3.302e+23, 2.439e6),
        VENUS(4.869e+24, 6.052e6),
        EARTH(5.975e+24, 6.378e6),
        MARS(6.419e+23, 3.393e6),
        JUPITER(1.899e+27, 7.149e7),
        SATURN(5.685e+26, 6.027e7),
        URANUS(8.683e+25, 2.556e7),
        NEPTUNE(1.024e+26, 2.477e7),
        ;
    
        private final double mass;
        private final double radius;
        private final double surfaceGravity;
    
        private static final double G = 6.67300E-11;
    
        Planet(double mass, double radius) {
            this.mass = mass;
            this.radius = radius;
            surfaceGravity = G * mass / (radius * radius);
        }
    
        public double getMass() { // 질량
            return mass;
        }
    
        public double getRadius() { // 반지름
            return radius;
        }
    
        public double getSurfaceGravity() { // 행성의 중력
            return surfaceGravity;
        }
    
        public double getSurfaceWeight(double mass) { // 행성의 무게, 질량 * 중력
            return mass * surfaceGravity; // F = ma
        }
    }
    ```
    
    ```java
    		public static void main(String[] args) {
            Planet mercury = Planet.MERCURY;
    
            System.out.println("Fields : ");
            for (Field field : mercury.getClass().getDeclaredFields()) {
                int modifiers = field.getModifiers();
                String modifierText = Modifier.toString(modifiers);
    
                System.out.println(modifierText + " " + field.getName());
            }
            System.out.println();
    
            System.out.println("Methods : ");
            for (Method method : mercury.getClass().getDeclaredMethods()) {
                int modifiers = method.getModifiers();
                String modifierText = Modifier.toString(modifiers);
    
                System.out.println(modifierText + " " + method.getName());
            }
        }
    ```
    
    ```java
    Fields : 
    public static final MERCURY
    public static final VENUS
    public static final EARTH
    public static final MARS
    public static final JUPITER
    public static final SATURN
    public static final URANUS
    public static final NEPTUNE
    private final mass
    private final radius
    private final surfaceGravity
    private static final G
    private static final $VALUES
    
    Methods : 
    public static values
    public static valueOf
    private static $values
    public getMass
    public getRadius
    public getSurfaceGravity
    public getSurfaceWeight
    ```
    
    이렇게 하면 특이한 점은 Planet.mercury 의 필드를 조회하는 데
    
    다른 행성들이 나온다는 것인데, 이는 Planet 의 각각의 상수들이
    
    `public static final` 로 선언되어 있기 때문에
    
    각각의 객체도 이에 대해 알고 있는 것이다!
    

# 필드/메서드 추가시 지켜야 할 규칙

1. 내부적으로만 사용할 필드/메서드라면 == 내구 구현에 해당한다면 private, default 로 선언하자.
2. global 한 enum 은 Top-Level 이 두고,
    
    하나의 클래스에서 사용한다면 필드로 선언하자.
    
3. toString 을 오버라이딩 한다면, 동시에 fromString 도 오버라이딩 하자.

# 필드/메서드를 추가하는 경우 1 - 상수마다 다른 행동

사칙연산 Enum Type(+,-,*,) 에 각 상수마다의 연산 메서드 `operate`를 넣는다고 생각한다면,

이때 각 상수별로 `operate` 함수는 다르게 동작해야 할 것이다.

이때 상수별로 switch에 따른 operate 함수 구현하는 방식도 방법이겠지만

이를 추상메서드로 선언하고, 각자 구현하는 게 

훨씬 더 안전하고 유지보수성(상수추가? → case 추가해야 되기 때문에)이 높다

```java
public enum LambdaOperation {
    PLUS((x, y) -> x + y),  // PLUS(DOUBLE::sum) 도 가능
    MINUS((x, y) -> x - y),
    TIMES((x, y) -> x * y),
    DIVIDE((x, y) -> x / y);

    private final DoubleBinaryOperator func;

    LambdaOperation(DoubleBinaryOperator func) {
        this.func = func;
    }

    public double calc(int x, int y) {
        return func.applyAsDouble(x, y);
    }
}
```

(책에서는 calc 추상 메서드를 선언하고,  일일히 함수 블럭에서 구현을 했는 데

심각하게 중복인게 불-편해서 람다의 형태로 간단하게 만들었다.)

# 필드/메서드를 추가하는 경우 2 - 상수끼리의 코드 공유가 어렵다!

> 어줍잖은 코드 공유로 인한 정수기 코드 발생

서민재 (1996 ~)
> 
- 안 좋은 경우
    
    직원의 일당을 날별로 계산해주는 Enum Type NoHolidayPayrollDay 이다.
    
    뭐 작동이 하기도 하고, 나쁘지도 않다.
    
    ```java
    public enum NoHolidayPayrollDay {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;
    
        private static final int MINS_PER_SHIFT = 8 * 60;
    
        int pay(int minutesWorked, int payRate) {
            int basePay = minutesWorked * payRate;
            int overtimePay;
    
            switch (this) {
                case SATURDAY:
                case SUNDAY:
                    overtimePay = basePay / 2;
                    break;
                default:
                    overtimePay = minutesWorked <= MINS_PER_SHIFT ? 0 : (minutesWorked - MINS_PER_SHIFT) * payRate / 2;
            }
    
            return basePay + overtimePay;
        }
    }
    ```
    
    하지만 이 경우에 공휴일의 개념이 섞여 들어간다면
    
    유지보수가 좋지 않음을 느낄 수 있다.
    
    1. case 추가해야 한다.
    2. HOLIDAY 상수를 추가해도, MONDAY || HOLIDAY 의 경우 어떻게?
    
    같은 문제들이 대두된다.
    
    이런 경우 해결할 수 있는 방법은 2가지이다.
    
    1. 각각의 상수에다 일일히 중복 구현하기
        
        ```java
        MONDAY(int pay...}
        ...
        SUNDAY{int pay...}
        HOLIDAY{int pay...}
        ```
        
        하지만 중복이 정말 많다!!
        
    2. 도우미 메서드를 작성해서 한다
        
        → 쉽게 얘기하면 basePay(), overtimePay() 를 구현하고
        
        overtimePay 안에 switch case 추가하는 거다.
        
        ```java
        int basePay(){...}
        
        int overtimePay(){
        	switch(this):
        		case SATURDAY
        		case SUNDAY
        		case HOLIDAY
        ...
        }
        ```
        
        위에서 얘기했듯 switch 를 쓰게 되니, 안 좋다.
        
    
- 좋은 경우
    
    ```java
    public enum HolidayIncludedPayrollDay {
    
        MONDAY(WEEKDAY), TUESDAY(WEEKDAY), WEDNESDAY(WEEKDAY), THURSDAY(WEEKDAY), FRIDAY(WEEKDAY),
        SATURDAY(WEEKEND), SUNDAY(WEEKEND);
    
        private final PayType payType;
    
        HolidayIncludedPayrollDay(PayType payType) {
            this.payType = payType;
        }
    
        int pay(int minutesWorked, int payRate, boolean isHoliday) {
            PayType currPayType = isHoliday ? PayType.HOLIDAY : payType;
            return currPayType.pay(minutesWorked, payRate);
        }
    
        enum PayType { // PayType 으로 지급을 분류화 함 
            WEEKDAY {
                int overtimePay(int minsWorked, int payRate) {
                    return minsWorked <= MINS_PER_SHIFT ? 0 : (minsWorked - MINS_PER_SHIFT) * payRate / 2;
                }
            },
            WEEKEND {
                int overtimePay(int minsWorked, int payRate) {
                    return minsWorked * payRate / 2;
                }
            },
            HOLIDAY { // 휴일에는 초과 수당을 10 배로 준다고 해보자.
                int overtimePay(int minsWorked, int payRate) {
                    return minsWorked * payRate * 10;
                }
            };
    
            private static final int MINS_PER_SHIFT = 8 * 60;
    
            abstract int overtimePay(int mins, int payRate);
    
            int pay(int minsWorked, int payRate) {
                int basePay = minsWorked * payRate;
                return basePay + overtimePay(minsWorked, payRate);
            }
        }
    }
    ```
    

# Switch 를 쓰는 예외 경우

위는 본인이 Enum Type 을 직접 선언하는 경우

내부 구현을 직접 수정할 수 있을 때 사용하지만

외부 라이브러리의 Enum Type 을 가져다 사용해야 하는 경우에는

switch 를 써야할 때도 있다.

혹은 기존의 Enum Type 의 상수별 메서드 추가가 단순 기능 강화(책에서는 상수별 동작을 혼합하여 넣는 경우라고 하는 데, 원서에서는 augment 라고 표현이 되어 있고, 이게 더 맞는 표현이라고 생각해서 이렇게 표현)

정도 수준이라면 그냥 switch 를 써도 상관이 없다.