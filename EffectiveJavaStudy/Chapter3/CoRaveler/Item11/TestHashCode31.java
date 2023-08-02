package CoRaveler.Item11;

public class TestHashCode31 {
    public static void main(String[] args) {
        String s1 = "abc";
        String s2 = "cab";

        int res1 = 0;
        int res2 = 0;

        res1 += "a".hashCode();
        res1 += 2 * res1 + "b".hashCode();
        res1 += 2 * res1 + "c".hashCode();
        System.out.println("res1 = " + res1);

        res2 += "c".hashCode();
        res2 += 2 * res2 + "a".hashCode();
        res2 += 2 * res2 + "b".hashCode();
        System.out.println("res2 = " + res2);
        System.out.println();

    }
}
