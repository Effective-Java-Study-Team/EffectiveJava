
import java.util.List;
import java.util.Objects;
import java.util.Optional;

class CompareUtils {

    private CompareUtils() {}

    public static <E extends Comparable<E>> Optional<E> max(List<E> c) {
        if (c.isEmpty()) {
            return Optional.empty();
        }

        E result = null;
        for (E e : c) {
            if (result == null || e.compareTo(result) > 0) {
                result = Objects.requireNonNull(e);
            }
        }

        return Optional.of(result);
    }

    public static <E extends Comparable<? super E>> Optional<E> maxRefactor(List<? extends E> list) {
        if (list.isEmpty()) {
            return Optional.empty();
        }

        E result = null;
        for (E e : list) {
            if (result == null || e.compareTo(result) > 0) {
                result = Objects.requireNonNull(e);
            }
        }

        return Optional.of(result);
    }
}
