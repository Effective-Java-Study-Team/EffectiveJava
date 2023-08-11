package callback;

final class SomeService {

    void performAsync(SomethingWithCallback callback) {
        new Thread(() -> {
            System.out.println(callback.getClass().getName());
            perform();
            callback.call();
        }).start();
    }

    void perform() {
        System.out.println("performed!");
    }
}
