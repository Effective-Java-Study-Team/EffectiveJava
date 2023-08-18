package CoRaveler.Item24;

import java.util.ArrayList;

public class BadMapExample {
    public ArrayList<MapEntry> entries = new ArrayList<>();

    class MapEntry {
        private Object key;
        private Object value;

        public MapEntry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        public Object getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public void setKey(Object key) {
            this.key = key;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        @Override
        public String toString() {
            String result = "key : " + key + ", value : " + value + ", MapExample = " + BadMapExample.this;
            return result;
        }
    }

    public void putEntry(Object key, Object value) {
        entries.add(new MapEntry(key, value));
    }

    public void printAllElements() {
        for (MapEntry me : entries)
            System.out.println(me);
    }
}
