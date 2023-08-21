package CoRaveler.Item23;

public class StructedClass {

}

abstract class Shape {
    int x, y;
    abstract double area();
}

class Circle extends Shape {
    final double radius;

    Circle(double radius) {
        this.radius = radius;
    }

    @Override
    double area() {
        return Math.PI * radius * radius;
    }
}

class Rectangle extends Shape {
    final double width;
    final double length;

    Rectangle(double width, double length) {
        this.width = width;
        this.length = length;
    }

    @Override
    double area() {
        return width * length;
    }
}