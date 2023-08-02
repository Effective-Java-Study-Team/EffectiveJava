public class PrivateInstance {

    // 인스턴스 통제 클래스

    private static final PrivateInstance INSTANCE = new PrivateInstance();

    private PrivateInstance() {}

    public static PrivateInstance getInstance() {
        return INSTANCE;
    }
}


