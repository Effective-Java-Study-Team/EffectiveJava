package CoRaveler.Item13;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ArrayClone {
    public static void main(String[] args) {
        String[] strArr = {"abc"};
        System.out.println("(strArr.clone() == strArr) = " + (strArr.clone() == strArr));
    }
}
