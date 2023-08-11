import java.util.Collection;
import java.util.Set;

public class InheritanceSet<E> extends ForwardingSet<E> {

    private int addCount = 0;

    public InheritanceSet(Set<E> s) {
        super(s);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }

    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    public int getAddCount() {
        return addCount;
    }


}
