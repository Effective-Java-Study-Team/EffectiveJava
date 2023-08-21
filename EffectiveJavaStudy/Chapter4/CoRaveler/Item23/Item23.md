# 아이템23 - 태그 달린 클래스보다는 클래스 계층구조를 활용하라

태그: In progress

# 1. 태그 달린 클래스란?

```java
public class TaggedFigure {
    enum Shape {RECTANGLE, CIRCLE}

    // 태그 필드
    final Shape shape;

    int x, y; // 중심 좌표값

    double length;
    double width;

    double radius;

    // 원 생성자
    TaggedFigure(double radius) {
        shape = Shape.CIRCLE;
        this.radius = radius;
    }

    // 직사각형 생성자
    TaggedFigure(double width, double length) {
        shape = Shape.RECTANGLE;
        this.width = width;
        this.length = length;
    }

    void printShape() {
        System.out.println(shape);
    }

    double area() {
        switch (shape) {
            case RECTANGLE:
                return length * width;
            case CIRCLE:
                return Math.PI * radius * radius;
            default:
                throw new AssertionError(shape);
        }
    }
}
```

Figure 클래스는 shape 라는 태그 필드를 통해 

다양한 의미의 객체를 표현할 수 있다.

⇒ 즉, `특정 타입이나 상태에 따라 다르게 동작하는 메서드나 속성을 가진 클래스` 라고 볼 수 있다.

# 2. 태그 달린 클래스의 단점은?

- 단점들
    1. **`불필요한 코드 많음`**
        
        → 열거 타입 선언, 태그 필드, switch 문 등의 불필요한 코드가 많아진다.
        
    2. **`가독성 저하`**
        
        → 여러 구현이 한 클래스에 혼합되어 있어 코드의 가독성이 나빠진다.
        
    3. **`메모리 사용 증가`**
        
        → 다른 의미를 위한 코드도 항상 함께 있어 메모리 사용량이 늘어난다.
        
    4. **`필드 초기화 문제`**
        
        → 필드들을 final로 선언하려면 사용하지 않는 필드도 생성자에서 초기화해야 한다. 
        이는 불필요한 코드가 늘어나게 만든다.
        
    5. **`컴파일러 지원 부족`**
        
        → 생성자가 태그 필드를 설정하고 해당 의미에 사용되는 데이터 필드를 초기화할 때, 
        컴파일러가 큰 도움을 주지 못하고, 잘못된 필드 초기화는 런타임에 문제를 발생시킨다.
        
    6. **`확장성 문제`**
        
        → 새로운 의미를 추가할 때마다 관련 코드를 수정해야 하며, 모든 switch 문을 찾아 새 의미를 처리하는 코드를 추가해야 한다. 하나라도 빠뜨리면 런타임 오류가 발생할 수 있다.
        
    7. **`의미 파악 어려움`**
        
        → 인스턴스의 타입만으로는 해당 인스턴스가 나타내는 의미를 알기 어렵다.


![Untitled](/Users/xpmxf4/Desktop/develop/EffectiveJava/EffectiveJavaStudy/Chapter5/CoRaveler/Item23/pictures/많기도하네.png)
    

한 마디로 정리하자!

<aside>
💡 **태그 달린 클래스는 장황하고, 오류를 내기 쉽고, 비효율적이다!

따라서, 태그 달린 클래스가 있다면 99% 클래스 계층 구조로 리팩토링 하자!**

</aside>

<aside>
💡 저자가 대놓고 클래스 계층 구조의 **`아류`라고 칭함!**

</aside>

# 3. 태그 달린 클래스 → 클래스 계층구조로의 전환

1. root 가 될 추상 클래스 정의
    
    ```java
    abstract class Figure
    ```
    
2. 태그 값에 따라 달라지는 메서드들은 추상메서드로 선언
    
    ```java
    abstract class Figure {
    		abstract double area();
    }
    ```
    
3. 태그에 상관없이 동일하게 작동하는 메서드는 디폴트 메서드로 추가
    
    ```java
    abstract class Figure {
    		abstract double area();
    
    		void printShape() {
    				System.out.println(shape);
    		}
    }
    ```
    
4. 모든 하위 클래스에서 공통으로 사용되는 데이터 필드들도 전부 root class로
    
    ```java
    abstract class Figure {
    		int x, y;   // 중심 좌표값
    
    		abstract double area();
    
    		void printShape() {
    				System.out.println(shape);
    		}
    }
    ```
    
5. 루트 클래스를 확장한 구체 클래스들을 하나씩 정의한다
    
    ```java
    Circle extends Figure
    Rectangle extends Figure
    ```
    
6. 추상 메서드를 각각에서 구현
    
    ```java
    class Circle extends Figure {
    		final double radius;
    
    		Circle(double radius) {
    				this.radius = radius;
    		}
    		
    		@Override
    		double area() {
    				return Math.PI * radius * radius;
    		}
    }
    
    class Rectangle extends Figure {
    		final double width;
    		final double height;
    
    		Rentangle(double width, double height) {
    				this.width = width;
    				this.height = height;
    		}
    
    		@Override
    		double area() {
    				return width * height;
    		}
    }
    ```
    

# 4. 전환시의 장점

- 장점s
    1. **`간결하고 명확하다`**: 코드가 더 간결해지며, 불필요한 코드나 중복성이 줄어든다.
    2. **`불필요한 코드 제거`**: 쓸데없는 코드가 사라져 관리하기가 쉽다.
    3. **`독립된 클래스 구조`**: 각 의미를 독립된 클래스에 담아서, 불필요한 데이터 필드를 제거할 수 있다.
    4. **`데이터 필드의 안전성`**: 남아 있는 필드들은 **`final`**로 선언될 수 있어 변경이 불가능하게 되면서 데이터의 안전성이 높아진다.
    5. **`컴파일러의 체크`**: 클래스의 생성자가 필드를 제대로 초기화했는지, 추상 메서드를 제대로 구현했는지 등을 컴파일러가 검사해준다.
    6. **`런타임 오류 최소화`**: 빠트린 case 문 때문에 발생하는 런타임 오류를 피할 수 있다.
    7. **`확장성`**: 루트 클래스의 코드를 수정하지 않고도 다른 프로그래머들이 독립적으로 계층구조를 확장하고 사용할 수 있다.
    8. **`의미별 타입 제한`**: 타입이 의미별로 구분되어 있으므로, 변수의 의미를 명시하거나 제한할 수 있으며, 특정 의미만을 매개변수로 받을 수 있다.
    9. **`타입의 계층 관계 반영`**: 타입 사이의 자연스러운 계층 관계를 표현할 수 있어 유연성이 증가하며, 컴파일타임에서의 타입 검사 능력도 향상된다.
    10. **`간단한 확장`**: 예로 든 정사각형 같은 새로운 타입을 추가하기도 쉽다.

  ![Untitled](/Users/xpmxf4/Desktop/develop/EffectiveJava/EffectiveJavaStudy/Chapter5/CoRaveler/Item23/pictures/많기도하네.png)
    

<aside>
🤙🏻 태그 달린 클래스를 써야 하는 상황은 거의 없다!

새로운 클래스를 작성하는 데 태그 필드가 등장한다면
태그를 없애고 계층구졸 대채하는 방법을 생각해보자.

기존 클래스가 태그 필드를 사용하고 있다면 계층구조로
리팩터링하는 걸 고민해보자.

</aside>