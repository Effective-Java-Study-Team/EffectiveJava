public class StackTest {

    public static void main(String[] args) {
        MyStack stack = new MyStack();
        stack.push("111");
        stack.push(1);

        // Exception in thread "main" java.lang.ClassCastException:
        // class java.lang.Integer cannot be cast to class java.lang.String
        String s = (String) stack.pop();
    }
}
