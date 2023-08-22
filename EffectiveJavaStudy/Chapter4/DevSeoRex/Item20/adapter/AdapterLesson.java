package adapter;

public class AdapterLesson {

    public static void main(String[] args) {
        Volt110 volt110 = Volt110.getInstance();
        Connection.connect(VoltAdapter.translateVolt110ToVolt220(volt110));
    }
}
