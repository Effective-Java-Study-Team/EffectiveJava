package DevSeoRex.Item13;


public class SubClass extends ParentClass {

    int a;

    public SubClass(int a) {
        super(a);
        this.a = a;
    }

    @Override
    public SubClass clone() throws CloneNotSupportedException {
        return (SubClass) super.clone();
    }

    @Override
    protected void someLogic() {
        super.a = 0;
    }

    @Override
    public String toString() {
        return "SubClass{" +
                "a=" + a + ", super.a=" + super.a +
                '}';
    }
}
