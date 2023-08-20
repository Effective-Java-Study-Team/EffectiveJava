package DevSeoRex.Item24;

public class NonStaticMember {

    int a = 20;

    void print() {
        System.out.println("Outer = " + a);
    }

    class Inner {
        int a = 10;

        public void print() {
            System.out.println("Inner = " + a);
            System.out.println("Outer.a = " + NonStaticMember.this.a);
        }
    }

    static class StaticInner {

        int a = 30;

        public void print() {
            System.out.println("Inner = " + a);
//            System.out.println("Outer.a = " + NonStaticMember.this.a); // Compile Error

        }
    }
}
