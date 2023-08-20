### 아이템 23 - 태그 달린 클래스보다는 클래스 계층구조를 활용하라

### 태그 달린 클래스를 사용하면 안되는 이유

두 가지 이상의 의미를 표현할 수 있으며, `현재 표현하는 의미`를 `태그 값`으로 알려주는 클래스가 있다.

```java
class Figure {

		
    enum Shape { RECTANGLE, CIRCLE }

    // 태그 필드 - 현재 모양을 나타낸다.
    final Shape shape;

    // 아래 필드들은 모양이 사각형일때만 쓰인다.
    double length;
    double width;

    // 이 필드는 모양이 원일때만 쓰인다.
    double radius;

    // 원용 생성자
    Figure(double radius) {
        this.radius = radius;
        this.shape = Shape.CIRCLE;
    }

    // 사각형용 생성자
    Figure(double length, double width) {
        this.length = length;
        this.width = width;
        this.shape = Shape.RECTANGLE;
    }

    double area() {
        return switch (shape) {
            case RECTANGLE -> width * length;
            case CIRCLE -> Math.PI * (radius * radius);
            default -> throw new AssertionError(shape);
        };
    }
    
}
```

이 클래스는 단점이 가득하다.
먼저 `열거 타입 선언, 태그 필드, switch 문` 등 쓸데없는 코드가 많이 작성되어 있다. <br>
이 클래스를 클래스 계층 구조를 활용해 작성했다면 전혀 필요 없는 것들을 가지고 있는 것이다.

이 클래스는 `사각형과 원`을 표현할 수 있는 `태그 달린 클래스`인데, 원만 사용하는 필드와 사각형만 사용하는 필드가 항상 메모리 위에 올라와 있어야 하는 것도 문제다.

지금 `length, width, radius` 필드는 `final` 키워드가 붙지 않아서 필수적으로 초기화를 해야하지는 않지만, <br>
`final` 키워드를 활용한다면 사용하지 않는 필드까지도 초기화를 해야하는 상황이 발생하는것이다.

`엉뚱한 필드를 초기화`했다고 해도 `런타임에 에러`가 나거나, 예상치 않은 값이 나와서 알 수 있게 될 뿐이다.

만약 이런 상황에서 오각형이나 다른 도형을 표현해야 한다면, `열거형에 타입도 추가`해야 하고 `area`를 계산하는 <br>
`메서드의 swtich문 수정`과 `필드추가` `생성자 추가` 등 4가지의 이유로 `코드 변경`이 이루어진다.

이 클래스의 문제는 끝이 없는데, 또 다른 문제점으로 `인스턴스의 타입` 만으로는 현재 나타내는 의미를 알 길이 없다는 것이다.

```java
Figure circle = new Figure(2.3);
Figure rectangle = new Figure(2.2, 3.3);
```

`변수명`에서 어떤 의미를 나타내려고 하는지 충분히 알 수 있다. <br>
하지만 `타입으로는 이것이 어떤 의미`를 나타내려고 하는지 도무지 알 수 없다는 것이다.

```java
Figure a = new Figure(2.3);
Figure b = new Figure(2.2, 3.3);
```

`변수명`에서 의미를 표현하지 않으면, 어떤 것이 원이고 어떤 것이 사각형을 의미하는지 `알 수가 없다.` <br>
태그 달린 클래스는 `장황`하고, `오류`를 내기 쉽고 `비효율적`이기 까지 하다.

### 태그 달린 클래스 → 클래스 계층 구조로 리팩토링 하자.

`객체 지향 언어`는 타입 하나로 다양한 의미의 객체를 표현할 수 있다. <br>
클래스 계층구조를 활용하는 `서브 타이핑`을 이용하는 방법이다.

서브 타이핑은 `부모 타입의 참조변수`로 `자식 인스턴스`를 가리킬 수 있고, `상속`을 통한 `기능확장`을 이룰 수 있는 객체지향 언어의 특징이다.

태그 달린 클래스를 클래스 계층구조로 바꾸는 방법은 간단하다.

- 계층 구조의 `루트` 클래스를 정의한다.
    - 태그 값에 따라 `동작이 달라지는 메서드`들을 루트 클래스의 `추상 메서드`로 선언한다.
    - 태그 값에 상관없이 `동작이 일정한 메서드`들을 루트 클래스에 `일반 메서드`로 추가한다.
    - 모든 하위 클래스에서 `공통으로 사용하는 데이터 필드` 가 있다면 루트 클래스로 올린다.

```java
abstract class AbstractFigure {

    abstract double area();
}

class Circle extends AbstractFigure {
    final double radius;

    Circle(double radius) {
        this.radius = radius;
    }

    @Override
    double area() {
        return Math.PI * (radius * radius);
    }
}

class Rectangle extends AbstractFigure {

    final double length;
    final double width;

    Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }

    @Override
    double area() {
        return length * width;
    }
}
```

태그 달린 클래스에서 추상 클래스로 루트 클래스를 정의하고, <br>
하위 클래스에서는 `메서드를 오버라이딩` 하고, `필요한 필드`들을 가질 수 있게 구조를 변경했다.

모든 필드에 `final 키워드`를 사용해서 `컴파일 타임`에 `생성자`가 모든 필드를 초기화 했는지 검사할 수 있다. <br>
`case 문`에서 조건을 추가 하지 않은 문제로 인해 `런타임 오류`가 발생할 일도 사라졌다.

타입이 의미별로 따로 존재하기 때문에 `변수`의 의미를 `명시`하거나 `제한`할 수 있게 되었다.

```java
// 변수명이 아닌 타입으로 어떤 의미를 가지고 있는지 알 수 있다.
Rectangle a = new Rectangle(2, 3.3);
Circle b = new Circle(2.4);

// 부모의 타입 변수로 자식을 활용 -> 다형성 활용 가능
AbstractFigure circle2 = new Circle(2.4);
AbstractFigure rectangle2 = new Rectangle(2.6, 2.8);
```

`클래스 계층`  구조를 활용하면 `다형성`과 `상속`의 이점을 누릴 수 있고, 타입으로 어떤 의미가 있는지 알 수 있다. <br>
유연성을 높여주고 컴파일타임 `타입 검사 능력도 향상`시켜준다는 장점도 있다.

```java
class Square extends Rectangle {
    Square(double side) {
      super(side, side);
    }
}
```

`정사각형` 클래스를 추가하려면 이렇게 코드를 작성하면 간단히 추가할 수 있다. <br>
`태그 달린 클래스`에서는 이 `정사각형` 의미를 추가하려면 최소 4군데 이상의 `코드 추가` 또는 `수정`이 필요하다.

- **정리**
1. 태그 달린 클래스를 써야 하는 상황은 거의 없다.
2. 새로운 클래스를 작성하는 데 태그 필드가 등장한다면, `계층구조로 대체`하는 것을 고려해보자.
3. 기존 클래스가 태그 필드를 사용한다면 `계층구조로 리팩터링` 하는 것을 고려해보자.
