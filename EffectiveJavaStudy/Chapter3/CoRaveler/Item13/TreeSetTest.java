package CoRaveler.Item13;

import java.util.TreeSet;

public class TreeSetTest {
    public static void main(String[] args) {
        TreeSet<Integer> tree1 = new TreeSet<>();
        System.out.println(tree1.getClass());           // class java.util.TreeSet
        System.out.println(tree1.clone().getClass());   // class java.util.TreeSet
//        TreeSet<Integer> tree2 = tree1.clone();         // TreeSet.clone 의 return 타입은 Object 라서 안된다!
    }
}
