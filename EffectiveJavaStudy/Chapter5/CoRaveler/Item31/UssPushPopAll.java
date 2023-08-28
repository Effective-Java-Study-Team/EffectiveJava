package CoRaveler.Item31;

import java.util.ArrayList;
import java.util.Collection;

public class UssPushPopAll {
    public static void main(String[] args) {
        BadGenericStackPushPopALL<Number> stack = new BadGenericStackPushPopALL<>();
        Iterable<Integer> integers = new ArrayList<>();
        Collection<Object> c = new ArrayList<>();

        // 안 좋은 경우
//        stack.pushAll(integers);
//        stack.popAll(c);

        // 좋은 경우
        GoodGenericStackPushPopAll<Number> stack2 = new GoodGenericStackPushPopAll<>();
        stack2.pushAll(integers);
        stack2.popAll(c);
    }
}
