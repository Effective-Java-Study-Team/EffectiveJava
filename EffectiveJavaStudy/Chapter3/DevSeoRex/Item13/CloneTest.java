import java.util.ArrayList;

public class CloneTest {

    public static void main(String[] args) throws CloneNotSupportedException {
        Market market1 = new Market("superMarket", new ArrayList<>());

        // Cloneable 인터페이스를 구현하지 않으면 동작하지 않고 예외발생
        Market market2 = market1.clone();
    }
}
