package simulateInheritance;

public abstract class AbstractTouchScreen implements TouchAble {

    @Override
    public void touch() {
        System.out.println("touch!!");
    }

    public abstract void updateTouch();
}
