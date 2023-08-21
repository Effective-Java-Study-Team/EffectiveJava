import java.util.Set;

public class CollectionProblem {

    static void add1(Set<?> set, Object o) {
//        set.add(o);
    }

    static void add2(Set<? super Object> set, Object o) {
        set.add(o);
    }

    static <E> void add3(Set<E> set, E e) {
        set.add(e);
    }
}
