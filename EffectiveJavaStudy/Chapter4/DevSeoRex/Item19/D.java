public class D extends C {

    int z;

    public D(int x, int y, int z) {
        super(x, y);
        this.z = z;
    }

    @Override
    public void overrideMe() {
        super.x = 0;
    }

    @Override
    public String toString() {
        return "D{" +
                "z=" + z +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
