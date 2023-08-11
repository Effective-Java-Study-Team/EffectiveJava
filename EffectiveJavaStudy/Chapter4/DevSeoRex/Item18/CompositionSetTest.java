import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CompositionSetTest {

    public static void main(String[] args) {

        Set<Integer> integerSet = new InheritanceSet<>(new HashSet<>());
        Set<String> stringSet = new InheritanceSet<>(new LinkedHashSet<>());

        integerSet.addAll(List.of(1, 2, 3));
        stringSet.addAll(List.of("1", "2"));

        System.out.println("integerSet.size() = " + integerSet.size());  // 3
        System.out.println("stringSet.size() = " + stringSet.size());   // 2

    }
}
