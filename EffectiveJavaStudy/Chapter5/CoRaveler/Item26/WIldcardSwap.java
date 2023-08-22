package CoRaveler.Item26;

import java.util.ArrayList;
import java.util.List;

public class WIldcardSwap {

    static void processList(List list) {
        // ... processing
    }

    static void processListWildcard(List<?> list) {
        // ... processing
    }

    static void processListObject(List<Object> list) {
        // ... processing
    }

    public static void main(String[] args) {
        List<String> strList = new ArrayList<>();
        List<Integer> intList = new ArrayList<>();

        processList(strList);
        processList(intList);

        processListWildcard(strList);
        processListWildcard(intList);

        // 에러가 난다!
//        processListObject(strList);
//        processListObject(intList);
    }
}
