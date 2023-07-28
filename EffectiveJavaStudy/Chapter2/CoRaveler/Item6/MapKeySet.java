package CH2.Item6;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.Set;

public class MapKeySet {
    public static void main(String[] args) {
        Map<Integer, Integer> map = new HashMap();
        map.put(1, 10);
        map.put(2, 20);
        map.put(3, 30);
        map.put(4, 40);
        map.put(5, 50);
        Set<Integer> set1 = map.keySet();
        Set<Integer> set2 = map.keySet();

        System.out.println(set1);
        System.out.println(set2);
        System.out.println("(set1==set2) = " + (set1==set2));
        map.put(6, 60);
        System.out.println("after");
        System.out.println(set1);
        System.out.println(set2);
        System.out.println("(set1==set2) = " + (set1==set2));
    }
}
