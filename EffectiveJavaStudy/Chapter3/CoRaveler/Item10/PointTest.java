package CoRaveler.Item10;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class PointTest {
    public static void main(String[] args) {
//         ColorPoint, SmellPoint 의 stackOverFlow
        ColorPoint cp = new ColorPoint(1, 2, Color.RED);
        SmellPoint sp = new SmellPoint(1, 2, "stink");

        cp.equals(sp);

        // onUnitCircle 문제
//        CounterPoint counterPoint = new CounterPoint(0, 1);
//        System.out.println("Point.onUnitCircle(counterPoint) = " + Point.onUnitCircle(counterPoint));
    }
}

class Point {
    private final int x, y;

    // 단위 원 안의 모든 점을 포함호도록 unitCircle을 초기화한다.
    private static final Set<Point> unitCircle = Set.of(new Point(1, 0), new Point(0, 1), new Point(-1, 0), new Point(0, -1));

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
//        if (!(obj instanceof Point)) return false;
//        Point p = (Point) obj;
//
//        return this.x == p.x && this.y == p.y;
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        Point p = (Point) o;
        return p.x == x && p.y == y;
    }

    public static boolean onUnitCircle(Point p) {
        return unitCircle.contains(p);
    }
}


class ColorPoint extends Point {
    private final Color color;

    public ColorPoint(int x, int y, Color color) {
        super(x, y);
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point)) {
            return false;
        }

        // o가 ColorPoint 가 아닌 Point 라면 좌표만 비교
        if (!(o instanceof ColorPoint)) {
            return o.equals(this);
        }

        // o가 ColorPoint 인 경우
        return super.equals(o) && this.color == ((ColorPoint) o).color;
    }
}

class SmellPoint extends Point {
    private String smell;

    public SmellPoint(int x, int y, String smell) {
        super(x, y);
        this.smell = smell;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point)) {
            return false;
        }

        // o가 SmellPoint 가 아닌 Point 라면 좌표만 비교
        if (!(o instanceof SmellPoint)) {
            return o.equals(this);
        }

        // o가 SmellPoint 인 경우
        return super.equals(o) && this.smell.equals(((SmellPoint) o).smell);
    }
}

class CounterPoint extends Point {
    private static final AtomicInteger counter = new AtomicInteger();

    public CounterPoint(int x, int y) {
        super(x, y);
        counter.incrementAndGet();
    }

    public static int numberCreated() {
        return counter.get();
    }
}

enum Color {
    RED, BLUE, GREEN,
};