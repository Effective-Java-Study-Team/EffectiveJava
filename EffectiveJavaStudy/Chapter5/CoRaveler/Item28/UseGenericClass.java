package CoRaveler.Item28;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UseGenericClass {
    public static void main(String[] args) {
        GenericClass<String> genericClass = new GenericClass<>(String.class, 10);
        System.out.println("Arrays.toString(genericClass.arr) = " + Arrays.toString(genericClass.arr));
//        GenericClass<String> genericClass1 = new GenericClass<>(Integer.class, 10);

        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        System.out.println("Arrays.toString(list.toArray(new Object[10])) = " + Arrays.toString(list.toArray(new Integer[10]))); // error
    }
}
