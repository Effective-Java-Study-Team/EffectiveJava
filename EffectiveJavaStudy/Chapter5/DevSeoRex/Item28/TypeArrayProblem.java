import java.util.ArrayList;
import java.util.List;

public class TypeArrayProblem {

    public static void main(String[] args) {
        Object[] objectArray = new Long[1];
        // Exception in thread "main" java.lang.ArrayStoreException: java.lang.String
        objectArray[0] = "타입이 일치하지 않아 넣을 수 없다!";

        // 컴파일 조차 되지 않는다.
//        List<Object> objectList = new ArrayList<Long>();

    }
}
