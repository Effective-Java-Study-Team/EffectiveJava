package CoRaveler.Item13;

import java.util.Arrays;
import java.util.EmptyStackException;

public class StackTest implements Cloneable {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public StackTest() {
        this.elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public Object pop() {
        if (size == 0) throw new EmptyStackException();
        Object result = elements[--size];
        elements[size] = null; // 다 쓴 참조 해제 return result;
        return result;
    }

    // 원소를 위한 공간을 적어도 하나 이상 확보한다.
    private void ensureCapacity() {
        if (elements.length == size) elements = Arrays.copyOf(elements, 2 * size + 1);
    }

    @Override
    public StackTest clone() {
        try {
            StackTest result = (StackTest) super.clone();
            System.out.println("result.elements = " + result.elements);
            System.out.println(elements == result.elements);
            result.elements = elements.clone(); // 내용물은 같은 새로운 배열 return
            System.out.println("result.elements = " + result.elements);
            return result;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

