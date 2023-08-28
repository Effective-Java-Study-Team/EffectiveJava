package CoRaveler.Item30;

import java.util.Collection;
import java.util.Objects;

public class RecursiveGenericMax {
    public static <E extends Comparable<E>> E max(Collection<E> c) {
        if (c.isEmpty())
            throw new IllegalStateException("컬렉션 비워있음");

        E result = null;

        for (E e : c) {
            System.out.println(result);
            if (result == null || e.compareTo(result) > 0) {
                result = Objects.requireNonNull(e);
            }
        }

        return result;
    }
}
