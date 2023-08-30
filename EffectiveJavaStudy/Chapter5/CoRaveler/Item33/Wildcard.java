package CoRaveler.Item33;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Wildcard {
    static List<?> list = new ArrayList<>();

    static void wildMethod(List<?> list) {
        System.out.println(list);
    }

    static void normalMethod(List<Object> list) {
        System.out.println(list);
    }

    public static void main(String[] args) {
        List<Integer> intList = List.of(42);

        wildMethod(list);
//        normalMethod(list);
    }
}
