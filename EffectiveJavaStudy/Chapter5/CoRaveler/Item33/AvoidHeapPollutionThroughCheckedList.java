package CoRaveler.Item33;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AvoidHeapPollutionThroughCheckedList {
    static void method(List<String>[] stringList) {
        List<Integer> intList = List.of(42);
        Object[] objects = stringList;
        objects[0] = intList;
        System.out.println(stringList[0].get(0));   // runtime 때 잡음
    }

    public static void main(String[] args) {
        List<String> strList = new ArrayList<>();
        List<String> checkedStrList = Collections.checkedList(strList, String.class);
        List<String>[] arr = new List[1];
        arr[0] = checkedStrList;
        method(arr);
    }
}
