package decorator;

public class DecoratorTest {

    public static void main(String[] args) {

        Coffee latte = new Latte(new Water(new BaseCoffeeComponent()));
        System.out.println(latte.add());

        Coffee americano = new Water(new BaseCoffeeComponent());
        System.out.println(americano.add());

        Coffee espresso = new BaseCoffeeComponent();
        System.out.println(espresso.add());


    }
}
