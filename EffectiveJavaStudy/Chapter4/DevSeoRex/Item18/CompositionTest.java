import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CompositionTest {

    public static void main(String[] args) {

        InheritanceSet<String> set1 = new InheritanceSet<>(new TreeSet<>());
        InheritanceSet<String> set2 = new InheritanceSet<>(new HashSet<>());

        set1.addAll(List.of("a", "b", "c"));
        set2.addAll(List.of("a", "b", "c"));

        System.out.println("set1.getAddCount() = " + set1.getAddCount()); // 3
        System.out.println("set2.getAddCount() = " + set2.getAddCount()); // 3
    }
}
