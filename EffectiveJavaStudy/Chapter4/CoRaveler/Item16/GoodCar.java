package CoRaveler.Item16;

public class GoodCar {
    private double speed;

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void accelerate() {
        this.speed += 10;
    }
}
