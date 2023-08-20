package adapter;

public class Volt110 {

    public static final int volt = 110;

    private Volt110() {}


    public static Volt110 getInstance() {
        return new Volt110();
    }
}
