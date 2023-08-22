package templateMethod.interface_template;

public class SecondJob extends AbstractJob {
    @Override
    public void setup4() {
        System.out.println("Second Job = setup4");
    }

    @Override
    public void setup5() {
        System.out.println("Second Job = setup5");
    }
}
