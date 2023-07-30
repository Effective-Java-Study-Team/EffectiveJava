package ch2.item7;

import java.util.WeakHashMap;

public class WeakHashMapTest {

    public static void main(String[] args) {


        WeakHashMap<Integer, String> studentMap = new WeakHashMap<>();
        Integer key1 = 300;
        Integer key2 = 400;

        studentMap.put(key1, "student1");
        studentMap.put(key2, "student2");

        studentMap.entrySet()
                        .forEach(System.out::println);

        key1 = null;
        key2 = null;

        System.gc();
        System.out.println("After GC");

        studentMap.entrySet()
                .forEach(System.out::println);


    }
}


