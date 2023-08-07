public class DefaultInterfaceImpl implements DefaultInterface {
    @Override
    public void print(int num) {

    }

    @Override
    public void print(String x) {
        DefaultInterface.super.print(x);
    }
}
