package CoRaveler.Item31;

import CoRaveler.Item30.RecursiveGenericMax;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;

public class UseGenericMax {
    public static void main(String[] args) {
        List<ScheduledFuture<Integer>> list = new ArrayList<>();
//        RecursiveGenericMax.max(list);

        System.out.println(max(list));
    }

    public static <E extends Comparable<? super E>> E max(Collection<E> c) {
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
