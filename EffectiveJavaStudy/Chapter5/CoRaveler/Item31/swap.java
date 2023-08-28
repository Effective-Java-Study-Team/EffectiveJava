package CoRaveler.Item31;

import java.util.List;

public class swap {
    public static void main(String[] args) {

    }


    public static <E> void swap1(List<E> list, int i, int j) {
        list.set(i, list.set(j, list.get(i)));
    }

    public static void swap2(List<?> list, int i, int j) {
        swapHelper(list, i, j);
    }

    private static <E> void swapHelper(List<E> list, int i, int j) {
        list.set(i, list.set(j, list.get(i)));
    }
}
