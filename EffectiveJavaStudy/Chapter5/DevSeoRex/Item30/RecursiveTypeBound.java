import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class RecursiveTypeBound {


    static <E extends Comparable<E>> Optional<E> max(Collection<E> c) {
        // 예외를 던지는 대신 빈 Optional을 반환해주는 것이 좋다.
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

}
