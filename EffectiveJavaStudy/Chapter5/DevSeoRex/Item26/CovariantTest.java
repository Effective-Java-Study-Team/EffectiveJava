import java.util.ArrayList;
import java.util.List;

public class CovariantTest {

    public static void main(String[] args) {

        // ArrayStoreExcetpion 발생!
        Object[] arr = new Long[1];
        arr[0] = 1;

        // Compile Error 발생!
//        List<Object> list = new ArrayList<Long>();
    }
}
