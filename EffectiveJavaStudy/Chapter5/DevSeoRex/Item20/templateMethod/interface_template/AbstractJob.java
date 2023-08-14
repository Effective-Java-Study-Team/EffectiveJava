package templateMethod.interface_template;

public abstract class AbstractJob implements Jobs {

    int a;
    int b;

    void set() {
        setup1();
        setup2();
        setup3();
        setup4();
        setup5();
    }

    public abstract void setup4();
    public abstract void setup5();
}
