package CoRaveler.Item29;

import java.util.Arrays;
import java.util.EmptyStackException;

public class GenericMyStack2<T> {
    private Object[] elements;
    private int size;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public GenericMyStack2() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(T o) {
        ensureCapacity();
        elements[size++] = o;
    }

    // 1. elements 는 private
    // 2. push 로 T 타입만 들어온다.
    // -> 따라서 unchecked 는 보장됩니다.
    public T pop() {
        if (size == 0)
            throw new EmptyStackException();
        @SuppressWarnings("unchecked") T result = (T) elements[--size];
        elements[size] = null;
        return result;
    }

    private void ensureCapacity() {
        if (elements.length == size)
            elements = Arrays.copyOf(elements, 2 * size + 1);
    }
}
