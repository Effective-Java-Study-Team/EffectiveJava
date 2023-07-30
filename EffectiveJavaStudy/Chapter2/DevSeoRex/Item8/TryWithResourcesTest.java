package ArrayProblem;

public class TryWithResourcesTest {

    public static void main(String[] args) {
        try (Room room = new Room(7)) {
            System.out.println("방청소!");
        }

    }
}

