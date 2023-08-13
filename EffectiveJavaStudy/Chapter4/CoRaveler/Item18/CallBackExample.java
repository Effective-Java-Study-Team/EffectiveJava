package CoRaveler.Item18;

public class CallBackExample {
    public static void main(String[] args) {
        OriginalClass originalClass = new OriginalClass();
        WrapperClass wrapperClass = new WrapperClass(originalClass);

        wrapperClass.doSomethingAndRegister();
    }
}
