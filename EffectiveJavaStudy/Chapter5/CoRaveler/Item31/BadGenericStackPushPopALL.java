package CoRaveler.Item31;

import CoRaveler.Item29.GenericMyStack1;

import java.util.Collection;

public class BadGenericStackPushPopALL<E> extends GenericMyStack1<E> {
    public void pushAll(Iterable<E> src) {
        for(E e : src)
            push(e);
    }

    public void popAll(Collection<E> c) {
        while(isEmpty())
            c.add(pop());
    }
}
