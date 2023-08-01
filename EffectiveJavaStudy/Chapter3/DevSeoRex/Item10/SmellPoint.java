public class SmellPoint extends Point{


    public SmellPoint(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean equals(Object o) {
        // Point의 인스턴스가 아니면 Point와 ColorPoint 어느 것에도 속하지 않는다 -> false
        if (!(o instanceof Point)) return false;


        if (!(o instanceof SmellPoint)) return o.equals(this);

        return super.equals(o);
    }
}
