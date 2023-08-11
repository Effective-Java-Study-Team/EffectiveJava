import java.util.List;

public class InheritanceSetTest {

    public static void main(String[] args) {

        InstrumentedHashSet<String> set = new InstrumentedHashSet<>();
        set.addAll(List.of("a", "b", "c"));

        System.out.println("set.getAddCount() = " + set.getAddCount()); // 6이 출력된다.
    }
}
