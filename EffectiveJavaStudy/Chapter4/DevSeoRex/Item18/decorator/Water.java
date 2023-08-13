package decorator;

public class Water extends CoffeeDecorator {


    public Water(Coffee coffee) {
        super(coffee);
    }

    public String add() {
        return super.add() + " + water";
    }
}
