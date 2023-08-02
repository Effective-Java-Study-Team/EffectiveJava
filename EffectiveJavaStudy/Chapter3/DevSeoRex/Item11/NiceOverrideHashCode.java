import java.util.HashMap;
import java.util.Map;

public class NiceOverrideHashCode {

    public static void main(String[] args) {

        NiceHashCode code1 = new NiceHashCode(100, 100);
        NiceHashCode code2 = new NiceHashCode(100, 100);
        NiceHashCode code3 = new NiceHashCode(100, 50);

        System.out.println("code1 = " + code1.hashCode());
        System.out.println("code2 = " + code2.hashCode());
        System.out.println("code3 = " + code3.hashCode());

        // hashCode를 equals 규약에 맞게 재정의하면 Hash 기반의 컬렉션 인터페이스들도 정확하게 동작한다.
        Map<NiceHashCode, String> map = new HashMap<>();

        map.put(code1, "code1");
        map.put(code3, "code3");

        System.out.println(map.get(code2));
        System.out.println(map.get(code3));
    }
}
