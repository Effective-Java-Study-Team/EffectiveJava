package templateMethod.abstract_class;

public class SetupFirst extends AbstractStep {
    @Override
    void step1() {
        System.out.println("step1");
    }

    @Override
    void step2() {
        System.out.println("step2");
    }

    @Override
    void step3() {
        System.out.println("step3");
    }

    @Override
    void step4() {
        System.out.println("setup first = step4");
    }

    @Override
    void step5() {
        System.out.println("setup first = step5");
    }
}
