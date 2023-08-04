package CoRaveler.Item14;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class CallPhoneNumber {
    public static void main(String[] args) {
        PhoneNumber pn = new PhoneNumber(010, 1111, 2222);
        PhoneNumber pn2 = new PhoneNumber(010, 3333, 3333);
        PhoneNumber pn3 = new PhoneNumber(010, 4444, 4444);
        PhoneNumber pn4 = new PhoneNumber(010, 4444, 5555);

        Set<PhoneNumber> ts = new TreeSet<>();
        ts.add(pn);
        ts.add(pn2);
        ts.add(pn3);
        ts.add(pn4);
        System.out.println(ts);
        System.out.println();

        Set<PhoneNumber> hs = new HashSet<>();
        hs.add(pn);
        hs.add(pn2);
        hs.add(pn3);
        hs.add(pn4);
        System.out.println(hs);
    }
}
