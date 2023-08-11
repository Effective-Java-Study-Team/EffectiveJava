package decorator;

abstract class CoffeeDecorator implements Coffee {

    private final Coffee coffee;

    CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }

    public String add() {
        return coffee.add();
    }
}
