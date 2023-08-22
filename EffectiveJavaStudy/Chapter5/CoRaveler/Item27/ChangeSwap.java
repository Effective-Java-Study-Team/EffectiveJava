package CoRaveler.Item27;

import java.util.ArrayList;
import java.util.List;

public class ChangeSwap {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            list.add(i);

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void swap(List<?> list, int i, int j) {
//        @SuppressWarnings("unchecked") List l = list; // 여긴 됨, 선언 + 일부러 raw type 쓴거라
        List l = list;
        l.set(i, l.set(j, l.get(i)));   // 여기에서 raw type 인데 set 쓰니까
                                        // 또 unchecked warning 이 뜨지만
                                        // 선언부가 아니라서 @SuppressWarnings("unchecked") 사용이 안된다.
    }
}
