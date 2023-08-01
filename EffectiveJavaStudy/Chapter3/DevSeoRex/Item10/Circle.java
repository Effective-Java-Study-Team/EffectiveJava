public class Circle {

    private int radius;
    private double area;

    public Circle(int radius) {
        this.radius = radius;
        this.area = Math.PI * radius * radius;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;

        if (!(o instanceof Circle circle)) return false;

        return Double.compare(area, circle.area) == 0 && radius == circle.radius;
    }
}

