package ch2.item1;

public class FactoryMethodCar {

    private int price;
    private String name;
    private String brand;
    private int leftGas;

    private FactoryMethodCar(int price, String name, String brand, int leftGas) {
        this.price = price;
        this.name = name;
        this.brand = brand;
        this.leftGas = leftGas;
    }

    public static FactoryMethodCar getInstance(int price, String name, String brand, int leftGas) {
        return new FactoryMethodCar(price, name, brand, leftGas);
    }

    public static FactoryMethodCar benzFactoryCar() {
        return new FactoryMethodCar(5000, "프리미엄 부릉이", "벤츠", 400);
    }

    public static FactoryMethodCar hyundaiFactoryCar() {
        return new FactoryMethodCar(2000, "부릉이", "현대", 200);
    }
}
