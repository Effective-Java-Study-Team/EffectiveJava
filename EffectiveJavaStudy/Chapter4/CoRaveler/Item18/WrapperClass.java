package CoRaveler.Item18;

public class WrapperClass implements Callback {
    private final OriginalClass ori;

    public WrapperClass(OriginalClass ori) {
        this.ori = ori;
    }


    @Override
    public void execute() {
        System.out.println("Wrapper Class's execute()");
        ori.execute();
    }

    public void doSomethingAndRegister() {
        ori.wrongRegister();
    }
}
