import java.util.*;

public class StackTest2 {

    public static void main(String[] args) {
        BeforeRefactorStack<Number> stack = new BeforeRefactorStack<>();
        Set<Integer> set = Set.of(1, 2, 3, 4);
        // java: incompatible types: java.util.Set<java.lang.Integer>
        // cannot be converted to java.lang.Iterable<java.lang.Number>
//        stack.pushAll(set);

        AfterRefactorStack<Number> stack2 = new AfterRefactorStack<>();
        Set<Integer> set2 = Set.of(1, 2, 3, 4);
        stack2.pushAll(set2);


        BeforeRefactorStack<Number> stack3 = new BeforeRefactorStack<>();
        stack3.push(1);
        stack3.push(2);
        Collection<Object> collection = Collections.emptyList();
        // java: incompatible types: java.util.Collection<java.lang.Object>
        // cannot be converted to java.util.Collection<java.lang.Number>
//        stack3.popAll(collection);

        AfterRefactorStack<Number> stack4 = new AfterRefactorStack<>();
        stack4.push(1);
        stack4.push(2);
        Collection<Object> collection2 = new ArrayList<>();
        stack4.popAll(collection2);
        System.out.println("collection2 = " + collection2);
    }
}
