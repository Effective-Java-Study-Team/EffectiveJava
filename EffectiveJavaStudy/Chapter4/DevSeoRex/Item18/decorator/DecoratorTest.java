package decorator;

public class DecoratorTest {

    public static void main(String[] args) {

        Coffee latte = new Latte(new Water(new BaseCoffeeComponent()));
        System.out.println(latte.add()); // espresso + water + Milk

        Coffee americano = new Water(new BaseCoffeeComponent());
        System.out.println(americano.add()); // espresso + water

        Coffee espresso = new BaseCoffeeComponent();
        System.out.println(espresso.add()); // espresso

        Coffee strangeCoffe = new Water(new Latte(new BaseCoffeeComponent()));
        System.out.println(strangeCoffe.add()); // espresso + Milk + water

    }
}
