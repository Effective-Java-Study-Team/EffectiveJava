package templateMethod.interface_template;

public class FirstJob extends AbstractJob {

    @Override
    public void setup4() {
        System.out.println("FirstJob = setup4");
    }

    @Override
    public void setup5() {
        System.out.println("FirstJob = setup5");
    }
}
