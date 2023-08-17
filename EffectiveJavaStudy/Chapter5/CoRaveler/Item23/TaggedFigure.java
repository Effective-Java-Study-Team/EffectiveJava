package CoRaveler.Item23;

public class TaggedFigure {
    enum Shape {RECTANGLE, CIRCLE}

    // 태그 필드
    final Shape shape;

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
