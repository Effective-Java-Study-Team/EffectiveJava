public class B extends A{

    int j;

    public B(int x, int y, int j) {
        super(x, y);
        this.j = j;
    }

    @Override
    public B clone() {
        return (B) super.clone();
    }

    @Override
    public void overrideMe() {
        super.x  = 10;
    }

    @Override
    public String toString() {
        return "B{" +
                "j=" + j +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
