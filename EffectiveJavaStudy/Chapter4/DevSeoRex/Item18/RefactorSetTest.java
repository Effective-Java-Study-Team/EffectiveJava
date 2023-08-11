import java.util.List;

public class RefactorSetTest {

    public static void main(String[] args) {
        RefactorHashSet<String> set = new RefactorHashSet<>();
        set.addAll(List.of("a", "b", "c"));

        System.out.println("set.getAddCount() = " + set.getAddCount()); // 3이 출력된다.
    }
}
