package DevSeoRex.Item13;

public class HashTable implements Cloneable{

    private Entry[] buckets = new Entry[30];

    private static class Entry {
        final Object key;
        Object value;
        Entry next;

        Entry(Object key, Object value, Entry next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        Entry deepCopy() {
            // 재귀 호출은 리스트의 원소 수 만큼 스택 프레임을 소비하므로 스택 오버플로를 일으킬 위험이 있다.
            return new Entry(key, value,
                    next == null ? null : next.deepCopy());
        }

        Entry deepCopy2() {

            // 스택 오버플로를 일으키지 않도록 내부 반복자를 이용하는 것이 좋다.
            Entry result = new Entry(key, value, next);
            for (Entry p = result; p.next != null; p = p.next) {
                p.next = new Entry(p.next.key, p.next.value, p.next.next);
            }

            return result;
        }
    }

    @Override
    public HashTable clone() throws CloneNotSupportedException {
        // 복제된 버킷은 자신만의 버킷 배열을 가지지만, 원본과 같은 연결 리스트를 참조하는 문제가 생긴다.
        HashTable result = (HashTable) super.clone();
        result.buckets = buckets.clone();

        // 기존 버킷의 Entry가 참조하는 연결 리스트를 순회하면서 Entry 객체를 새로 만들어서 넣어준다.
        result.buckets = new Entry[buckets.length];
        for (int i=0; i<buckets.length; i++) {
            if (buckets[i] != null) {
                result.buckets[i] = buckets[i].deepCopy();
            }
        }

        return result;
    }
}
