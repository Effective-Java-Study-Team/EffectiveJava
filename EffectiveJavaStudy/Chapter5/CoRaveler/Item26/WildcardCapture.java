package CoRaveler.Item26;

import java.util.ArrayList;
import java.util.List;

public class WildcardCapture {
    private static void swapFirstTwo(List<?> list) {
        swapHelper(list, 0, 1);
    }

    private static <T> void swapHelper(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    public static void main(String[] args) {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        System.out.println("(before)list1 = " + list1);
        swapFirstTwo(list1);
        System.out.println("(after)list1 = " + list1);

        System.out.println();

        List<String> list2 = new ArrayList<>();
        list2.add("one");
        list2.add("two");
        System.out.println("(before)list2 = " + list2);
        swapFirstTwo(list2);
        System.out.println("(after)list2 = " + list2);
    }
}
