import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MapSetTest {

    public static void main(String[] args) {


        WrapperInteger setElement1 = new WrapperInteger(10);
        WrapperInteger setElement2 = new WrapperInteger(20);

        Set<WrapperInteger> set = new HashSet<>();

        set.add(setElement1);
        set.add(setElement2);

        System.out.println("set = " + set);
        setElement2.setNumber(10);
        // 값을 바꿔서 Set 내부의 원소에 중복을 허용하게 되는 문제가 발생한다.
        System.out.println("set = " + set);

        WrapperInteger mapKey = new WrapperInteger(10);
        Map<WrapperInteger, Integer> map = new HashMap<>();
        map.put(mapKey, 10);
        System.out.println(map.get(mapKey));

        // Map의 Key 값을 변경해서 원소를 찾지 못하는 문제가 발생한다.
        mapKey.setNumber(20);
        System.out.println(map.get(mapKey));
    }
}
