import java.util.Arrays;
import java.util.Collection;
import java.util.EmptyStackException;

class BeforeRefactorStack<E> {

    private E[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    /*
    *  배열 elements는 push(E)로 넘어온 E 인스턴스만 담는다.
    *  따라서 타입 안전성을 보장하지만,
    *  이 배열의 런타임 타입은 E[]가 아닌 Object[]다!
    * */
    @SuppressWarnings("unchecked")
    public BeforeRefactorStack() {
        this.elements = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(E e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public E pop() {
        if (size == 0) throw new EmptyStackException();

        E result = elements[--size];
        elements[size] = null; // 다 쓴 참조 해제
        return result;
    }

    public void pushAll(Iterable<E> src) {
        for (E e : src) {
            push(e);
        }
    }

    public void popAll(Collection<E> dst) {
        while (!isEmpty()) {
            dst.add(pop());
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }
}
