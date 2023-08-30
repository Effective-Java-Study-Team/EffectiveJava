package CoRaveler.Item32;

import java.util.ArrayList;
import java.util.List;

public class Flatten {
    static <T> List<T> flatten(List<? extends T>... lists) {
        System.out.println("lists.getClass() = " + lists.getClass());
        List<T> result = new ArrayList<>();
        for (List<? extends T> list : lists)
            result.addAll(list);
        return result;
    }

    public static void main(String[] args) {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            list1.add(i);
            list1.add(i * 10);
        }

        List<Integer> result = flatten(list1, list2);
        System.out.println(result);
    }
}
