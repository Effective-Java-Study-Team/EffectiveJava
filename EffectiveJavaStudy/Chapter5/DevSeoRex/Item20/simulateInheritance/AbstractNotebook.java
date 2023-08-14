package simulateInheritance;

public abstract class AbstractNotebook implements Computer {

    @Override
    public void on() {
        System.out.println("on");
    }

    @Override
    public void off() {
        System.out.println("off");
    }

    abstract void charge();
}
