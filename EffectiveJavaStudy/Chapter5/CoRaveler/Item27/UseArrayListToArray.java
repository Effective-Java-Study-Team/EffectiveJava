package CoRaveler.Item27;

import java.util.ArrayList;
import java.util.Arrays;

public class UseArrayListToArray {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList();
        for (int i = 1; i <= 5; i++) {
            list.add(i);
        }

        Object[] arr = list.toArray(new Object[10]);
        System.out.println(Arrays.toString(arr));
        Object[] arr2 = list.toArray(new Object[4]);
        System.out.println(Arrays.toString(arr2));
    }
}
