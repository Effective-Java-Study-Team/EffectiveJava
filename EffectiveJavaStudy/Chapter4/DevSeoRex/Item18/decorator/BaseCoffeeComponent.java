package decorator;

public class BaseCoffeeComponent implements Coffee {
    @Override
    public String add() {
        return "espresso";
    }
}
