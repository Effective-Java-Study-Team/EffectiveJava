package DevSeoRex.Item24;

public class NonStaticTest {

    public static void main(String[] args) {
        NonStaticMember nonStaticMember = new NonStaticMember();
        nonStaticMember.print();
//        nonStaticMember.beforePrint(); private 멤버 외부 접근 불가
        NonStaticMember.Inner inner = nonStaticMember.new Inner();
        inner.print(); // Inner = 10, Outer.a = 20
    }
}
