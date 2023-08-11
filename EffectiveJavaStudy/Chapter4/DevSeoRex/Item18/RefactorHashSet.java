import java.util.Collection;
import java.util.HashSet;

public class RefactorHashSet<E> extends HashSet<E> {

    // 추가된 원소의 수
    private int addCount = 0;

    public RefactorHashSet() {}

    public RefactorHashSet(int initCap, float loadFactor) {
        super(initCap, loadFactor);
    }

    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    /*
    *   addAll 메서드를 주어진 컬렉션을 순회하며 add 메서드를 호출하는 것으로 변경하면,
    *   상위 클래스의 addAll 메서드가 add를 사용하는지와 상관없이 결과가 옳다.
    * */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        int count = 0;

        for (E e: c) {
            add(e);
        }

        return c.size() == count;
    }

    public int getAddCount() {
        return addCount;
    }
}
