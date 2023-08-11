package callback;

public class CallBackTest {

    public static void main(String[] args) {
        SomeService service = new SomeService();
        WrappedObject wrappedObject = new WrappedObject(service);
        Wrapper wrapper = new Wrapper(wrappedObject);

        wrapper.doSomething();
    }
}
