import java.awt.*;
import java.util.Objects;

public class ColorPoint extends Point {
    private Color color;

    public ColorPoint(int x, int y, Color color) {
        super(x, y);
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        // Point의 인스턴스가 아니면 Point와 ColorPoint 어느 것에도 속하지 않는다 -> false
        if (!(o instanceof Point)) return false;

        // ColorPoint의 인스턴스가 아니면 point의 인스턴스 이므로 색상을 무시하고 비교
        if (!(o instanceof ColorPoint)) return o.equals(this);

        return super.equals(o) & ((ColorPoint) o).color == color;
    }




}

