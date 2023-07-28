package CH2.Item6;

public class BooleanTest {
    public static void main(String[] args) {
        Boolean b1 = new Boolean("True");
        Boolean b2 = Boolean.valueOf("True");
        Boolean b3 = Boolean.valueOf("True");

        System.out.println("b1 = " + b1);
        System.out.println("b2 = " + b2);
        System.out.println("b3 = " + b3);
        System.out.println("(b2 == b3) = " + (b2 == b3));
    }
}
