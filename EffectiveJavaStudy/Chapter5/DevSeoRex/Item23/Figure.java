class Figure {



    enum Shape { RECTANGLE, CIRCLE }

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
