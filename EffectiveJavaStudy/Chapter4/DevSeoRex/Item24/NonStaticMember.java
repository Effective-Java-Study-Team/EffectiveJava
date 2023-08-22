package DevSeoRex.Item24;

public class NonStaticMember {

    private int a = 20;

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

    private void beforePrint() {
        System.out.println("프린트 준비");
    }


    private static class StaticInner {

        int a = 30;

        NonStaticMember staticMember = new NonStaticMember();

        public void print() {
            System.out.println("Inner = " + a);
            staticMember.beforePrint();
//            System.out.println("Outer.a = " + NonStaticMember.this.a); // Compile Error

        }
    }

}
