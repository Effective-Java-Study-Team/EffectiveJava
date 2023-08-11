package CoRaveler.Item18;

import java.util.Collection;
import java.util.Set;

public class WrapperInstrumentedClass<E> extends ForwardingSet<E> {
    private int addCount = 0;

    public WrapperInstrumentedClass(Set<E> s) {
        super(s);
    }

    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);    // ForwardingSet 의 add
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }
}
