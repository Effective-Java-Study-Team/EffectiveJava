public class StringInsensitive {

    private final String s;


    public StringInsensitive(String s) {
        this.s = s;
    }

    // 비교자는 직접 만들거나 자바에서 제공하는 것 중 선택해 사용할 수 있다.
    public int compareTo(StringInsensitive cis) {
        return String.CASE_INSENSITIVE_ORDER.compare(s, cis.s);
    }
}
