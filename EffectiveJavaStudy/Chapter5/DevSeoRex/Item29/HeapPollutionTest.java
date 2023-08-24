import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HeapPollutionTest {

    public static void main(String[] args) {


        List list = Collections.checkedList(new ArrayList<>(), String.class);

        list.add("111");
        // Attempt to insert class java.lang.Integer element into collection with element type class java.lang.String
        list.add(111);
    }
}
