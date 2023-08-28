package CoRaveler.Item28;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrayListToArray<T> {
    private static int[] intArr = new int[]{1, 2, 3, 4, 5};
    private static String[] strArr = new String[]{"a", "b", "c"};

    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 5; i++)
            list.add(i);
        String[] strArr = new String[5];
        list.toArray(strArr);
    }

    public static <T> T[] toArray(T[] a) {
        return (T[]) Arrays.copyOf(a, a.length);
    }
}
