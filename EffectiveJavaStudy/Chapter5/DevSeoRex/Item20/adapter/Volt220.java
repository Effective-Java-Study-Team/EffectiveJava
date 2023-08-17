package adapter;

public class Volt220 {

    public static final int volt = 220;

    private Volt220() {}

    public static Volt220 getInstance() {
        return new Volt220();
    }
}
