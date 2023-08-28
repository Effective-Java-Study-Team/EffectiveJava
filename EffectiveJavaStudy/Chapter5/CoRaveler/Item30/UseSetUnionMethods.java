package CoRaveler.Item30;

import java.util.HashSet;
import java.util.Set;

import static CoRaveler.Item30.SetUnionMethods.union;
import static CoRaveler.Item30.SetUnionMethods.genericUnion;
import static CoRaveler.Item30.SetUnionMethods.moreFlexibleGenericUnion;

public class UseSetUnionMethods {
    public static void main(String[] args) {
        Set<Integer> intSet1 = new HashSet<>();
        Set<Integer> intSet2 = new HashSet<>();
        Set<Double> doubleSet1 = new HashSet<>();

        for (int i = 1; i <= 5; i++) {
            intSet1.add(i);
            intSet2.add(i);
            doubleSet1.add(i + i / 10D);
        }
        System.out.println("intSet1 = " + intSet1);
        System.out.println("intSet2 = " + intSet2);
        System.out.println("doubleSet1 = " + doubleSet1);
        System.out.println();

        System.out.println("union(intSet1, intSet2) = " + union(intSet1, intSet2));
        System.out.println("genericUnion(intSet1, intSet2) = " + genericUnion(intSet1, intSet2));
//        System.out.println("genericUnion(intSet1, doubleSet1) = " + genericUnion(intSet1, doubleSet1));
        System.out.println("moreFlexibleGenericUnion(doubleSet1, intSet2) = " + moreFlexibleGenericUnion(doubleSet1, intSet2));
    }
}
