package DevSeoRex.Item13;

import java.util.HashSet;
import java.util.TreeSet;

public class SetTest {

    public static void main(String[] args) {
        HashSet<Integer> hashSet = new HashSet<>();
        hashSet.add(1);
        hashSet.add(2);

        TreeSet<Integer> treeSet = new TreeSet<>(hashSet);
        System.out.println(treeSet);
    }


}
