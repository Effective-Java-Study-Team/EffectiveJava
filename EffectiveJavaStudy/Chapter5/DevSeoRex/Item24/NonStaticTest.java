package DevSeoRex.Item24;

public class NonStaticTest {

    public static void main(String[] args) {
        NonStaticMember nonStaticMember = new NonStaticMember();
        NonStaticMember.Inner inner = nonStaticMember.new Inner();
        inner.print(); // Inner = 10, Outer.a = 20
    }
}
