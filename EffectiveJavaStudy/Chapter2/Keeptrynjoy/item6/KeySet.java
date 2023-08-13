package ch2.item6;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class KeySet {
    public static void main(String[] args) {

        Map<String, Object> map = new HashMap<>();
        
        map.put("food","burger");
        map.put("cloths","t-shirt");

        Set<String> set1 = map.keySet();
        Set<String> set2 = map.keySet();
        
        System.out.println(set1.equals(set2));

        set1.remove("food");

        System.out.println(set1.size());
        System.out.println(set2.size());

    }
}
