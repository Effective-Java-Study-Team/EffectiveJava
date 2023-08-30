package CoRaveler.Item33;

import java.util.HashMap;
import java.util.Map;

public class WildMap {
    static Map<?, ?> map = new HashMap<>();

    public static void main(String[] args) {
//        map.put(1, "abc");        // 이 경우, 타입을 알 수가 없으니까 capture of ? 이다!
    }
}
