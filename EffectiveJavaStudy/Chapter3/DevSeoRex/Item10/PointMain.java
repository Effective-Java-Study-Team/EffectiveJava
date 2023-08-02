import java.awt.*;
import java.util.Set;

public class PointMain {

    public static void main(String[] args) {
        CounterPoint cp = new CounterPoint(1, 0);
        System.out.println(unitCircle.contains(cp));

        Coffee coffee1 = new Americano(1000, "bean", true);
        Coffee coffee2 = new Americano(1000, "bean", true);
        Coffee coffee3 = new Coffee(1000, "bean") {
            @Override
            public boolean equals(Object o) {
                return super.equals(o);
            }
        };

        System.out.println(coffee3.getClass());

        System.out.println(coffee1.equals(coffee2));
        System.out.println(coffee1.equals(coffee3));
        System.out.println(coffee3.equals(coffee1));

    }

    private static final Set<Point> unitCircle = Set.of(
            new Point(1, 0), new Point(0, 1),
            new Point(-1, 0), new Point(0, -1)
    );

    public static boolean onUnitCircle(Point p) {
        return unitCircle.contains(p);
    }

}
