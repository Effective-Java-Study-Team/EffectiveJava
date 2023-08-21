import java.util.Set;

public class SetTest {

    public static void main(String[] args) {
        Set s1 = Set.of(1, "ss", true);
        Set s2 = Set.of(1.344, false, true);

        System.out.println(wildCardNumElementsInCommon(s1, s2));
    }

    static int numElementsInCommon(Set s1, Set s2) {
        int result = 0;
        s1.add("22222");
        for (Object o1 : s1) {
            if (s2.contains(o1)) result ++;
        }

        return result;
    }

    static int wildCardNumElementsInCommon(Set<?> s1, Set<?> s2) {
        int result = 0;
        // 컴파일 에러
//        s1.add("111111111");
        for (Object o1 : s1) {
            if (s2.contains(o1)) result ++;
        }

        return result;
    }

}
