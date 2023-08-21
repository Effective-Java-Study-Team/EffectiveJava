import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionImmutable {

    public static void main(String[] args) {
        Collection<?> collection = new ArrayList<>();
//        collection.add("1");
        collection.add(null);

        Collection<? super String> strings = new ArrayList<>();
        strings.add("11");
        strings.add(null);
//        strings.add(1);


    }

    private static void foo(List<?> i) {
        // 에러 발생 -> java: incompatible types: java.lang.Object cannot be converted to capture#1 of ?
//        i.set(0, i.get(0));
        doSomething(i);
    }

    private static <T> void doSomething(List<T> i) {
        i.set(0, i.get(0));
    }
}
