package CoRaveler.Item26;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WildCard {
    public static void main(String[] args) {
        List<String> strings = new ArrayList<>();
//        unsafeAdd(strings, Integer.valueOf(42));
        String s = strings.get(0);

        Set<?> set = new HashSet<>();
//        set.add(1);
    }

    private static void unsafeAdd(List list, Object o) {
        list.add(o);
    }

    static int badNumElementsInCommon(Set s1, Set s2) {    // 잘못된 사용법
        int result = 0;
        for (Object o : s1) {
            if (s2.contains(s1)) result++;
        }
        return result;
    }

    static int goodNumElementsInCommon(Set<?> s1, Set<?> s2) {
        int result = 0;
        for (Object o : s1) {
            if (s2.contains(o)) {
                result++;
            }
        }
        return result;
    }
}

