package DevSeoRex.Item28;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayProblem {

    public static void main(String[] args) {

        List<String> stringList = List.of("1", "2", "3");

        // class [Ljava.lang.Object; cannot be cast to class [Ljava.lang.String;
        // ([Ljava.lang.Object; and [Ljava.lang.String; are in module java.base of loader 'bootstrap')
//        String[] array = (String[]) stringList.toArray();
//        String[] array = stringList.toArray(new String[0]);

        String[] array = toArray(stringList, String.class);
        System.out.println(Arrays.toString(array)); // -> [1, 2, 3]

        dangerous(List.of("1", "2", "3", "4", "6"), List.of("2"), List.of("3"));
    }

    // Super Type Token 방식 -> 클래스 리터럴을 사용하여 제네릭 타입 정보를 유지하는 방법
    static <T> T[] toArray(List<T> list, Class<T> clazz) {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(clazz, list.size());

        for (int i=0; i<list.size(); i++) {
            array[i] = list.get(i);
        }

        return array;
    }

    @SafeVarargs
    static void dangerous(List<String>... args) {
        Object[] listArr = args;
        listArr[0] = List.of(1);
        String s = args[0].get(0);
    }

//    static <T> T[] getArr1() {
//        return new T[20];
//    }
//
//    static <E> List<E>[] getArr2() {
//        return new ArrayList<E> [20];
//    }
//
//    static List<String>[] getArr3() {
//        return new ArrayList<String>[30];
//    }
}
