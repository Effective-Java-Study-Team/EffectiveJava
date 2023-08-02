public class EqualsMain {

    public static void main(String[] args) {

        Person p1 = new Person(20, "REX");
        Person p2 = new Person(20, "REX");

        System.out.println("p1.eqauls(p2) ? " + (p1.equals(p2))); // false
    }

}


