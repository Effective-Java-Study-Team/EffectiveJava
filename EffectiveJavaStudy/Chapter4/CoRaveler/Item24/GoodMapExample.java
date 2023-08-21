package CoRaveler.Item24;

import java.util.ArrayList;

public class GoodMapExample {
    public ArrayList<GoodMapEntry> entries = new ArrayList<>();

    static class GoodMapEntry {
        private Object key;
        private Object value;

        public GoodMapEntry(Object key, Object value) {
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
//            String result = "key : " + key + ", value : " + value + ", MapExample = " + GoodMapExample.this;
            String result = "key : " + key + ", value : " + value;
            return result;
        }
    }

    public void putEntry(Object key, Object value) {
        entries.add(new GoodMapEntry(key, value));
    }

    public void printAllElements() {
        for(GoodMapEntry gme : entries){
            System.out.println(gme);
        }
    }
}
