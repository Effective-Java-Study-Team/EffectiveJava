package CoRaveler.Item13;

import java.util.HashMap;
import java.util.Map;

public class HashMapTest {
    public static void main(String[] args) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            map.put(i, i);
        }
        System.out.println(map);
        map.forEach((el1, el2) -> {
            System.out.println(el1);
            System.out.println(el2);
        });
    }
}
