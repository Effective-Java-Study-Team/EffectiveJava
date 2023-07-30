package ArrayProblem;

public class SubClass extends SuperClass {

    static SuperClass superClass;

    public SubClass(int value) {
        super(value);
    }

    @Override
    protected void finalize() {
        superClass = this;
    }
}



