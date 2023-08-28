import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GenericStackTest {

    public static void main(String[] args) {
        MyGenericStack<String> stack = new MyGenericStack<>();
        stack.push("111");
//        stack.push(1); // 컴파일 에러

        String s = stack.pop();
        System.out.println("s = " + s);

        Stack<String> stack2 = new Stack<>();
        stack2.push("111");

        while (!stack2.isEmpty()) System.out.println(stack2.pop().toUpperCase());

        List<String> stringList = new ArrayList<>();
        stringList.add("111");
        List<Integer> integerList = (List<Integer>) (Object) stringList;
        int a = integerList.get(0);
    }
}
