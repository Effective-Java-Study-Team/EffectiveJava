package CoRaveler.Item20;

import java.util.AbstractList;
import java.util.List;
import java.util.Objects;

public class SkeletonImpl {
    static List<Integer> intArrayAsList(int[] a) {
        Objects.requireNonNull(a);

        return new AbstractList<>() {
            @Override
            public Integer get(int index) {
                return a[index];
            }

            @Override
            public int size() {
                return a.length;
            }

            @Override
            public Integer set(int index, Integer val) {
                int oldVal = a[index];
                a[index] = val;
                return oldVal;
            }
        };
    }

    public static void main(String[] args) {
        AbstractList<Integer> test = (AbstractList<Integer>) intArrayAsList(new int[]{1, 2, 3, 4, 5});
        for (int i = 0; i < test.size(); i++)
            System.out.println("test.get(i) = " + test.get(i));
    }
}
