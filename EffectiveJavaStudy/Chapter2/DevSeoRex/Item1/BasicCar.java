package ch2.item1;

public class BasicCar {

    private int price;
    private String name;
    private String brand;
    private int leftGas;


    public BasicCar(int price, String name, String brand, int leftGas) {
        this.price = price;
        this.name = name;
        this.brand = brand;
        this.leftGas = leftGas;
    }




    @Override
    public String toString() {
        return "BasicCar{" +
                "price=" + price +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", leftGas=" + leftGas +
                '}';
    }
}
