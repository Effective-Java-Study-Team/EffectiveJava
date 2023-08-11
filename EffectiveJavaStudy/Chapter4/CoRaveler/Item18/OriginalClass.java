package CoRaveler.Item18;

public class OriginalClass implements Callback {

    @Override
    public void execute() {
        System.out.println("OriginalClass's execute()");
    }

    public void registerCallback(Callback callback) {
        System.out.println("callback = " + callback.getClass());
        callback.execute();
    }

    public void wrongRegister() {
        // SELF 문제의 발생: 이 'this'는 OriginalClass의 인스턴스를 가리킨다.
        // 따라서 이것을 사용하여 콜백을 등록하면 WrapperClass의 로직이 실행되지 않는다.
        registerCallback(this);
    }
}
