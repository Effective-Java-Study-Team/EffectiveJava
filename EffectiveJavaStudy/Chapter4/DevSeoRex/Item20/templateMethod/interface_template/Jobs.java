package templateMethod.interface_template;

public interface Jobs {

    default void setup1() {
        System.out.println("setup1");
    }

    default void setup2() {
        System.out.println("setup2");
    }

    default void setup3() {
        System.out.println("setup3");
    }

    void setup4();
    void setup5();

}
