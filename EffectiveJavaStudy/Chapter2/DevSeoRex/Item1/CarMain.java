package ch2.item1;

public class CarMain {

    public static void main(String[] args) {
        // 일반적인 public 생성자로 객체를 생성하면 반환될 객체의 특징을 알 수 없다는 단점이 있다.
        BasicCar hyundaiBasicCar = new BasicCar(2000, "부릉이", "현대", 200);
        BasicCar benzBasicCar = new BasicCar(5000, "프리미엄 부릉이", "벤츠", 400);

        // 정적 팩터리 메서드의 이름을 잘 지으면 반환될 객체의 특징을 알 수 있지만, getInstance 라는 이름 만으로는 반환될 객체의 특징을 알 수 없다.
        FactoryMethodCar hyundaiFactoryCar = FactoryMethodCar.getInstance(2000, "부릉이", "현대", 200);
        FactoryMethodCar benzFactoryCar = FactoryMethodCar.getInstance(5000, "프리미엄 부릉이", "벤츠", 400);

        // 정적 팩터리 메서드의 이름을 통해 어떤 객체가 반환될지 알 수 있다.
        FactoryMethodCar hyundaiCar = FactoryMethodCar.hyundaiFactoryCar();
        FactoryMethodCar benzCar = FactoryMethodCar.benzFactoryCar();

    }
}
