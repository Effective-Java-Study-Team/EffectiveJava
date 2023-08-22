package CoRaveler.Item26;

import java.util.*;

public class ListObject {
    public static void main(String[] args) {
        List<Object> listGeneric = new ArrayList<>();
        List listNonGeneric = new ArrayList();
        for (int i = 1; i <= 5; i++) {
            listGeneric.add(i);
            listNonGeneric.add(i);
        }

        iterGeneric(listNonGeneric);
        iterNonGeneric(listGeneric);
    }

    private static void iterGeneric(List<String> list) {
        for (Object o : list) { // wow
            System.out.println(o);
        }
        System.out.println();
    }

    private static void iterNonGeneric(List list) {
        for (Object o : list) {
            System.out.println(o);
        }
        System.out.println();
    }


}
