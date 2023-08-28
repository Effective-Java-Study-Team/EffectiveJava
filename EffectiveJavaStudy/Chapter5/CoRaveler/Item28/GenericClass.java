package CoRaveler.Item28;

import java.lang.reflect.Array;

public class GenericClass<T> {
    public final T[] arr;

    public GenericClass(Class<T> clazz, int size) {
//        arr = new T[size]; , 이렇게 하면은 에러가 나게 된다.
        @SuppressWarnings("unchecked") T[] arrToPut = (T[]) Array.newInstance(clazz, size);
        arr = arrToPut;
    }
}
