import java.util.ArrayList;
import java.util.List;

public class UnsafeTest {

    public static void main(String[] args) {
        List<String> strings = new ArrayList<>();
        unsafeAdd(strings, Integer.valueOf(42));
        String s = strings.get(0); // 컴파일러가 자동으로 형변환 코드를 넣어준다 -> 묵시적 형변환
    }

    private static void unsafeAdd(List list, Object o) {
        list.add(o);
    }

//    private static void unsafeAdd(List<Object> list, Object o) {
//        list.add(o);
//    }
}
