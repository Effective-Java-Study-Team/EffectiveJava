public interface DefaultInterface {

    private void print() {
        System.out.println("print");
    }

    default void print(String x) {
        System.out.println(x + "!!");
    }

    void print(int num);
}
