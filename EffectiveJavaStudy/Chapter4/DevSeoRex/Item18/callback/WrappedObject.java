package callback;

public class WrappedObject implements SomethingWithCallback {

    private final SomeService service;


    public WrappedObject(SomeService service) {
        this.service = service;
    }


    @Override
    public void doSomething() {
        service.performAsync(this);
    }

    @Override
    public void call() {
        System.out.println("WrappedObject callback!");
    }
}
