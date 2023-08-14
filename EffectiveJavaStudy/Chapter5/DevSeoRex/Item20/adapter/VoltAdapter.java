package adapter;

public class VoltAdapter {

    static Volt220 translateVolt110ToVolt220(Volt110 volt110) {
        System.out.println("110 Volt change to 220 Volt");
        return Volt220.getInstance();
    }
}
