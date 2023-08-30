package DevSeoRex.Item13;

public class A implements Cloneable{

    int a;

    public A(int a) {
        this.a = a;
//        System.out.println("a's constructor = " + this);
    }

    @Override
    protected A clone() throws CloneNotSupportedException {
        A a = (A) super.clone();
        System.out.println("a's clone  = " + a);
        System.out.println("a's clone a.this = " + this);
        return a;
    }
}
