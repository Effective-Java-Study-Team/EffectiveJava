package CH2.Item6;

public class StringError {
    public static void main(String[] args) {
//        String[] s = new String[Integer.MAX_VALUE/10];
//        for (int i = 0; i < Integer.MAX_VALUE/10; i++) {
//            s[i] = new String("abc");
//        }

        String[] s2 = new String[Integer.MAX_VALUE / 10];
        for (int i = 0; i < Integer.MAX_VALUE / 10; i++) {
            s2[i] = "abc";
        }
    }
}
