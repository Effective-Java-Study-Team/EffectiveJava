public class CanonicalTest {

    public static void main(String[] args) {
        CanonicalString instance1 = new CanonicalString("apple tree");
        CanonicalString instance2 = new CanonicalString("appletree");

        System.out.println(instance1.equals(instance2)); // true
    }
}


