import java.util.Arrays;
import java.util.EmptyStackException;

public class Stack implements Cloneable {
    private Object[] element;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        this.element = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object obj) {
        ensureCapacity();
        element[size++] = obj;
    }

    public Object pop() {
        if (size == 0) throw new EmptyStackException();
        Object result = element[--size];
        element[size] = null;
        return result;
    }

    private void ensureCapacity() {
        if (element.length == size) {
            element = Arrays.copyOf(element, 2 * size + 1);
        }
    }


    @Override
    protected Stack clone() throws CloneNotSupportedException {
        Stack stack = (Stack) super.clone();
        stack.element = element.clone();
        return stack;
    }

    @Override
    public String toString() {
        return Arrays.toString(element);
    }
}
