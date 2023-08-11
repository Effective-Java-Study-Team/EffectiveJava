public class A implements Cloneable {

    int x;
    int y;

    public A(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public A clone() {
        try {
            A clone = (A) super.clone();
            overrideMe();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void overrideMe() {}
}
