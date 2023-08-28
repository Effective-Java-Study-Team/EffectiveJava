package CoRaveler.Item28;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WildCardArray {
    public static void main(String[] args) {
        List<Integer> el = new ArrayList<>();
        el.add(1);
        el.add(2);

        List<Integer>[] intListArr = new List[1];
        List<?>[] intListArr2 = new List<?>[1];

        intListArr[0] = el;
        intListArr2[0] = el;

        System.out.println("Arrays.toString(intListArr) = " + Arrays.toString(intListArr));
        System.out.println("Arrays.toString(intListArr2) = " + Arrays.toString(intListArr2));
        System.out.println("intListArr[0] = " + intListArr[0]);
        System.out.println("intListArr2[0] = " + intListArr2[0]);
    }
}
