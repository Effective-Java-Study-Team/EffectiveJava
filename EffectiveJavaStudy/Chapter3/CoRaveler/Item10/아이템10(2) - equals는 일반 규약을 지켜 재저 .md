# 아이템10(2) - equals는 일반 규약을 지켜 재정의하라

상태: Done

# 3. 추이성

<aside>
💡 x → y, y → z 이 성립하면 x → z 도 성립

</aside>

주로 자식클래스가 상위클래스에 없는 필드를 확장하며,

위 규칙을 어기게 된다.

```java
class Point {
    private final int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
			if(!(o instanceof Point that))
				return false;
			
			return x == that.x && y == that.y;
    }

    public static boolean onUnitCircle(Point p) {
        return unitCircle.contains(p);
    }
}
```

위처럼 Point 클래스가 있다고 하고

```java
class ColorPoint extends Point {
	private final String color;
	// 여기서 equals 를 어떻게 오버라이딩 하다 추이성을 어길까?
}
```

### 1. 대칭성을 위배하는 경우 - 좌표+색깔 다 비교

```java
©Override 
public boolean equals(Object o) { 
	if (!(o instanceof ColorPoint))
		return false;

	return super.equals(o) && ((ColorPoint) o).color = color;
}
```

```java
Point p = new Point(1,2); // x라고 하자.
ColorPoint cp = new ColorPoint(1,2,"RED"); // y라고 하자.
```

`p.equals(cp) == true` 이지만 `cp.equals(cp) == false` 이다.

즉 x → y ,이지만 y → x 는 성립하지 않는다.

Point 하고 비교 시에 색깔을 고려하기 때문이다.

### 2. 추이성 위배 - Point 하고 비교할 때는 좌표만 비교하기

그렇다면 Point 하고 비교할 때는 색깔을 고려하지 않도록 해보자

```java
@Override
public boolean equals(Object o) {
	if(!(o instanceof Point)) // Point 계열이 아니면 바로 배제
		return false;

	if(!(o instanceof ColorPoint)) // Point 인 경우
		return o.equals(this);

	return super.equals(o) && ((ColorPoint) o).color == color; // ColorPoint 인 경우
}
```

이 방식은 아까의 대칭성은 지켜준다.

```java
ColopPoint cp1 = new ColorPoint(1,2,"RED"); // x
Point p = new Point(1,2); // y
ColorPoint cp2 = new ColorPoint(1,2,"BLUE"); // z
```

이때 `x→y , y→ z`는 좌표값이 같아 성립하지만

`x→z` 는 색깔이 다르기 때문에 성립하지 않는다.

또한 이 방식은 같은 방식으로 equals 를 구현한 SmellPoint 클래스를 만들고

```java
ColorPoint cp = new ColorPoint(1, 2, Color.RED);
SmellPoint sp = new SmellPoint(1, 2, "stink");

cp.equals(sp);
```

를 실행하게 되면 다음과 같은 결과를 얻게 된다

![스크린샷 2023-08-02 오후 7.12.31.png](https://github.com/Effective-Java-Study-Team/EffectiveJava/blob/main/EffectiveJavaStudy/Chapter3/CoRaveler/Item10/pictures/equalsStackOverFlow.png?raw=true)

```java
	if(!(o instanceof ColorPoint)) //  cp.equals(sp), 
																 // Point 이지만 ColorPoint 는 아닌 경우
		return o.equals(this); // 
```

이 경우 `o.equals()` 가

ColorPoint 는 SmellPoint.equals() 를,

SmellPoint 에서는 ColorPoint.equals() 를 불러

stackoverflow 가 일어난다.

### 3.리스코프 치환 법칙 위배 - 완벽히 자기 자신과 일치하는 클래스만 비교하기

애초에 부모에서 없던 필드를 확장하면서 동시에

부모, 자식의 equals 를 만족하게 만들수는 없다.

왜냐하면 equals 

<aside>
💡 그렇다면 instanceof 연산자 대신에 getClass() 를 사용하면 안되나? 
그러면 완전히 자기 자신의 타입하고만 비교하니, 일반 규약을 어길 수가 없자나!

</aside>

이렇게 생각할 수도 있지만, 이렇게 하면 리스코프 치환 법칙을 어기게 된다.

```java
@Override
public boolean equals(Object o) {
	if(o == null || o.getClass() != getClass())
		return false;
	Point p = (Point) o;
	return p.x == x && p.y == y;
}
```

일반 규약을 지킬 수는 있다.

하지만 getClass 와 instanceof 연산자에는 차이가 존재하는 게,

getClass 는 정확히 자기 자신과 같은 클래스 객체와 같아야 하고

```java
public final class Class<T> implements java.io.Serializable,
                              GenericDeclaration,
                              Type,
                              AnnotatedElement,
                              TypeDescriptor.OfField<Class<?>>,
                              Constable {...}
```

instanceof 연산자는 뒤에 오는 클래스의 자손들까지도 허용해준다.

위처럼 하게 된다면

SOLID 의 리스코프 치환 법칙 - `하위 타입은 언제든지 상위 타입을 대체할 수 있어야 한다` 

을 어기게 되는 데 코드로 알아보자.

```java
// Point.class 안
private static final Set<Point> unitCircle = Set.of( // (0,1),(1,0),(0,-1),(-1,0)
	new Point(1,0),
	new Point(-1,0),
	new Point(0,1),
	new Point(0,-1)
}

public static boolean isOnUnitCircle(Point p) { // 들어온 좌표가 단위원에 있나
	return unitCircle.contains(p);
}
```

```java
public class CounterPoint extends Point {
	private static final Atomiclnteger counter = new Atomiclnteger();
	
	public CounterPoint(int x, int y) { 
		super(x, y);
		counter.incrementAndGet(); 
	}
	
	public static int numberCreated() { return counter.get(); } 
}
```

다음과 같이 Point 클래스 안에 필드들이 존재하고

Point 를 상속한 CounterPoint 클래스가 있다고 해보자.

리스코프 치환 원칙에 따르면,

부모 클래스에서 중요 속성, 메서드에 있어서는 

하위 클래스에서도 잘 작동을 해야 한다.

```java
Point p1 = new Point(1,2);
CounterPoint cp = new CounterPoint(0,1);

Point.isOnUnitCircle(p1); // true
Point.isOnUnitCircle(cp); // false
```

Point 의 메서드인 `isOnUnitCircle` 에 대해서

contains 메서드는 equals 를 호출하고 이는 

내부적으로 getClass() 를 통해 비교해 , 다르다고 나오는 것이다.

이는 리스코프 치환 원칙이 어겨진 셈이다.

# 4. 일관성

<aside>
💡 x.equals(y) == true 라면, 두 객체가 수정되지 않는 한
앞으로도 계속 호출할 때마다 영원히 같아야 한다.

</aside>

보통 불변 객체를 만들어 사용하게 된다면 위 규칙은 항상 지켜진다.

또한 불변 객체는 멀티스레드에서도 안정적이고, 코드가 단순해진다.

따라서 불변 객체인 경우, 한번 같은 객체는 영원히 같을 것이다.

하지만 모든 클래스를 불변으로 만들 수는 없기 때문에

가변으로 만드는 경우가 생기고, 이때 가장 주의해야 할건

`equals 를 판단하는 기준에 신뢰할 수 없는 자원`이

끼어들게 해서는 안된다는 것이다.

신뢰할 수 없는 자원을 통해 equals 를 판단하는 예시를 보자.

```java
public class User {
    private String name;
    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;

        boolean internetStatus = checkInternetStatus();

        return age == user.age && Objects.equals(name, user.name) && internetStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    private boolean checkInternetStatus() { // 현실에서는 불안정한 인터넷 상태를 반환함
        int num = (int) (Math.random() * 50);

        if (num < 25) return true;
        else return false;
    }
}
```

즉 equals 의 판단 기준안에 결과를 보장할 수 없는 

외부에서의 상태를 가지고 equals 를 사용하게 된다면

equals 의 일관성을 보장할 수가 없다!

# 5. null-아님

<aside>
💡 (모든 객체).equals(null) == false 여야 한다

</aside>

사실 이거는 위에서 말한 모든 방법이 

애초에 검사하고 시작해서 못 지키가 어렵다.

다만 인지하면 좋은 점은 다음과 같다.

### 1. 초보의 코드 - 명시적 null 체크

```java
@Override
public boolean equals(Object o) {
	if ( o == null ) {
		return false
	
	// ...
}
```

명시적으로 null 체크를 해준다.

### 2. 좀 치는 자의 코드

```java
@Override
public boolean equals(Object o) {
	if(!(o instanceof MyType) {
		return false;
	MyType mt = (MyType) o;
	// ...
}
```

어차피 `instanceof MyType` 에서 끝난다.

# equals 잘 구현하는 방법

1. `== 연산자를 사용해 입력이 자기 자신의 참조`인지 확인한다.
2. `instanceof 연산자를 통해, 입력이 올바른 타입`인지 확인
3. 입력을 올바른 타입으로 `형변환`다.
4. 입력 객체와, 자기 자신의 대응되는 `‘핵심’ 필드들이 모두 일치하는지 하나씩 검사`한다.
만약 2 단계에서 `instanceof {interface}` 의 형태로 했다면,
필드뿐만 아니라 메서드도 체크해야 한다.
5. `Null 값`과 `복잡한 필드` 비교
    - null 값을 정상 값으로 취급하는 필드는 Objects.equals(object, object(null)) 로 비교해서 NullPointerException 예방
    - 비교하기 복잡or비용↑ 인 필드는 표준형을 만들어두자.
        
        ```java
        class Point {
        	int x, y;
        	String canonical = null;
        
        	//... 생성자
        
        	String getCanonical() {
        		this.canocial = x + ":" + y;
        	}
        
        	@Override
        	public boolean equals(Object o) {
        		if(!(o instanceof Point) 
        			return false
        		Point p = (Point) o;
        		return canonical.equals(p.canonical);
        	}
        }
        ```
        
        여기서는 단순히 x, y
        
6. `비교 순서`와 `필요 없는 비교 제외`
    
    비교 순서로는 
    
    - 다를 가능성이 커서 다 빨리 다름이 인지되거나
    - 비교하는 비용이 싸다
    
    다음과 같은 기준들로 하면 성능 향상을 바라볼 수 있다.
    

이제 위 규칙들을 반영한, 아~주 잘 만든 equals 를 보자.

```java
short areaCode, prefix, lineNum;

@Override
public boolean equals(Object o) // 1. == 연산자를 사용해 입력이 자기 자신의 참조인지 확인한다.
	if(o == this)
		return true;

	if(!(o instanceof PhoneNumber)) // 2. instanceof 연산자를 통해, 입력이 올바른 타입인지 확인
		return false;

	PhoneNumber pn = (PhoneNumber) o; // 3. 입력을 올바른 타입으로 형변환다.

	// 4. 입력 객체와, 자기 자신의 대응되는 ‘핵심’ 필드들이 모두 일치하는지 하나씩 검사한다.
	return pn.lineNum == lineNum && pn.prefix == prefix && pn.areaCode == areaCode;
}
```

# 추가 고려사항

1. equals 를 재정의할 땐 hashCode 도 반드시 재정의하자
2. 너무 복잡하게 해결하려고 하지 말자
    - 사실 필드들의 동치성만 체크해도 애지간하면 구현이 된다.
3. 무조건 Object 타입으로 파라미터를 받자.
    
    오버라이딩이라는 것은 메서드의 시그니처가 동일하지만,
    
    구현부를 다르게 하는 것이다.
    
    즉, 파라미터의 타입을 같게 해야 하는 데 
    
    만약 파라미터의 타입을 다르게 한다면 이는 오버로딩이기 때문이다.
