package decorator;

public class Latte extends CoffeeDecorator {

    public Latte(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String add() {
        return super.add() + " + Milk";
    }
}
