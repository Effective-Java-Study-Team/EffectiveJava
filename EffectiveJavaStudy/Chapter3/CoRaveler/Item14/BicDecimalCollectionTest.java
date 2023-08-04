package CoRaveler.Item14;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class BicDecimalCollectionTest {
    public static void main(String[] args) {
        BigDecimal bd1 = new BigDecimal("1.0");
        BigDecimal bd2 = new BigDecimal("1.00");
        BigDecimal bd3 = new BigDecimal("-3.0");
        BigDecimal bd4 = new BigDecimal("999999999");
        Set<BigDecimal> hashSet = new HashSet<>();
        Set<BigDecimal> treeSet = new TreeSet<>();

        System.out.println("(bd1 == bd2) = " + (bd1 == bd2));
        System.out.println("(bd1.equals(bd2)) = " + (bd1.equals(bd2)));
        System.out.println("bd1.signum() = " + bd1.signum());
        System.out.println("bd2.signum() = " + bd2.signum());
        System.out.println("bd3.signum() = " + bd3.signum());
        System.out.println("bd4.signum() = " + bd4.signum());
        System.out.println("bd1.hashCode() = " + bd1.hashCode());
        System.out.println("bd2.hashCode() = " + bd2.hashCode());
        System.out.println("(bd1.compareTo(bd2)) = " + (bd1.compareTo(bd2)));
        System.out.println("(bd1.compareTo(bd4)) = " + (bd1.compareTo(bd4)));

        System.out.println();
        hashSet.add(bd1);
        hashSet.add(bd2);

        treeSet.add(bd1);
        treeSet.add(bd2);

        System.out.println("hashSet = " + hashSet);
        System.out.println("treeSet = " + treeSet);
    }
}
