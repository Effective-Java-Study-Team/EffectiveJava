package CoRaveler.Item30;

import java.util.ArrayList;
import java.util.List;

public class UseRecursiveGeneric {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            list.add(i);
        }

        System.out.println("RecursiveGeneric.max(list) = " + RecursiveGeneric.max(list));
    }
}
