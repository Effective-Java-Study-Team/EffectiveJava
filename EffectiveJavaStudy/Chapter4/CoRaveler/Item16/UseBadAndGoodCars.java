package CoRaveler.Item16;

public class UseBadAndGoodCars {
    public static void main(String[] args) {
        BadCar badCar = new BadCar();
        badCar.speed = 50;
        badCar.accelerate();
        System.out.println("badCar.speed = " + badCar.speed);

        GoodCar goodCar = new GoodCar();
        goodCar.setSpeed(50);
        goodCar.accelerate();
        System.out.println("goodCar.getSpeed() = " + goodCar.getSpeed());

        // 이때 speed 단위를 mph = km * 1.60934 로 바꾸고 싶다?
    }
}
