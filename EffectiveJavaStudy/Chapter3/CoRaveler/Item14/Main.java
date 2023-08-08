package CoRaveler.Item14;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        Double d1 = 3.4;
        Double d2 = 3.400;
        BigDecimal bd1 = new BigDecimal("3.4");
        BigDecimal bd2 = new BigDecimal("3.400");

        System.out.println("d1.hashCode() = " + d1.hashCode());
        System.out.println("d2.hashCode() = " + d2.hashCode());
        System.out.println("(d1==d2) = " + (d1==d2));
        System.out.println("(d1.equals(d2)) = " + (d1.equals(d2)));
        System.out.println("(d1.compareTo(d2)) = " + (d1.compareTo(d2)));

        System.out.println();

        System.out.println("bd1.equals(bd2) = " + bd1.equals(bd2));
        System.out.println("bd1.compareTo(bd2) = " + bd1.compareTo(bd2));
    }
}
