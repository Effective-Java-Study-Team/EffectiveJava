package CoRaveler.Item31;

import java.util.HashSet;
import java.util.Set;

public class BadUnion {
    public static <E> Set<? extends E> union(Set<? extends E> s1, Set<? extends E> s2) {
        Set<E> result = new HashSet<>(s1);
        result.addAll(s2);
        return result;
    }

    public static void main(String[] args) {
        Set<Integer> s1 = new HashSet<>();
        Set<Number> s2 = new HashSet<>();
        for (int i = 1; i <= 5; i++) {
            s1.add(i);
            s2.add(i + 10);
        }

        Set<? extends Number> union = union(s1, s2);
    }
}
