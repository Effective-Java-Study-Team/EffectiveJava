package DevSeoRex.Item13;

public class B extends A {

    int b;

    public B(int a, int b) {
        super(a);
        this.b = b;
//        System.out.println("b's constructor = " + this);
    }

    @Override
    protected B clone() throws CloneNotSupportedException {
        B b = (B) super.clone();
        System.out.println("b's clone = " + b);
        System.out.println("b's clone b.this = " + this);
        return b;
    }

    @Override
    public String toString() {
        return "B{" +
                "b=" + b +
                ", a=" + a +
                '}';
    }
}
