import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class CollectionRawType {

    public static void main(String[] args) {

        // 이 컬렉션은 숫자만 넣을 수 있다.
        Collection collection = new ArrayList();

        // 매개변수화 된 컬렉션 타입을 이용하면 컴파일조차 되지 않는다.
//        Collection<Integer> collection = new ArrayList<>();

        // 숫자를 넣는다.
        collection.add(1);
        // 실수로 문자를 넣는다.
        collection.add("1");

        for (Iterator i = collection.iterator(); i.hasNext();) {
            Integer number = (Integer) i.next(); // ClassCastException이 발생한다.
        }
    }
}
