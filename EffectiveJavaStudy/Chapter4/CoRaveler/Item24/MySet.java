package CoRaveler.Item24;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;

public class MySet<T> extends AbstractSet<T> {
    ArrayList<T> list = new ArrayList<>();

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean add(T t) {
        return list.add(t);
    }

    @Override
    public Iterator<T> iterator() {
        return new MyIterator();
    }

    public class MyIterator implements Iterator<T> {    // 직접 iterator 를 구현해야 하는 경우
        private int currIdx = 0;

        @Override
        public boolean hasNext() {
            return currIdx < list.size();
        }

        @Override
        public T next() {
            return list.get(currIdx++); // 현재 인스턴스의 정보 필요
        }
    }
}