package CoRaveler.Item13;

public class Sub extends Super {
    private final int state;

    Sub(int state) {
        this.state = state;
    }

    @Override
    void overrideMe() {
        System.out.println("State: " + state);
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        Sub sub = new Sub(10);
        for (int i = 0; i < 100000; i++) {
            Sub clone = (Sub) sub.clone();
            clone.overrideMe();
        }
    }
}