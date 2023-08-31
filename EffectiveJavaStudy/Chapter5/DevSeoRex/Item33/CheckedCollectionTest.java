import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CheckedCollectionTest {

    public static void main(String[] args) {
        Set set = Collections.checkedSet(new HashSet<>(), String.class);
        set.add("aa");
        set.add(1);
    }
}
