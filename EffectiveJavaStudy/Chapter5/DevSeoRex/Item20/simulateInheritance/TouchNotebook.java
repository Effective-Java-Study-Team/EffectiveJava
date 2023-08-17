package simulateInheritance;

public class TouchNotebook extends AbstractNotebook implements TouchAble {

    private final TouchScreen touchScreen = new TouchScreen();

    @Override
    void charge() {
        System.out.println("charging start ... !");
    }

    @Override
    public void touch() {
        touchScreen.touch();
    }

    @Override
    public void updateTouch() {
        touchScreen.updateTouch();
    }


    private class TouchScreen extends AbstractTouchScreen {

        @Override
        public void updateTouch() {
            System.out.println("touch update");
        }
    }
}
