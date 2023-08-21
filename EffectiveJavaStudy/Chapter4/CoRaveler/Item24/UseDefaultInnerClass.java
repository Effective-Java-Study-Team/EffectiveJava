package CoRaveler.Item24;

public class UseDefaultInnerClass {
    public static void main(String[] args) {
        Outer outer = new Outer(1, 2);
        Outer.Inner inner = outer.new Inner(11, 12);
        System.out.println("outer = " + outer);
        System.out.println();
        inner.printOuterMember();
        System.out.println();
        outer.createInnerInstance();
    }
}
