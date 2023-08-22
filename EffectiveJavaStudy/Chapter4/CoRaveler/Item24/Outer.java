package CoRaveler.Item24;

public class Outer {
    private int outer_a = 1;
    private int outer_b = 2;

    public Outer(int outer_a, int outer_b) {
        System.out.println("Outer Class's Constructor Called.");
        System.out.println();
        this.outer_a = outer_a;
        this.outer_b = outer_b;
    }
    
    public void createInnerInstance() {
        Inner inner = new Inner(55, 66);
    }

    public void explicitCreateInner() {
        Outer.Inner explicitInner = this.new Inner(77, 88);
    }

    class Inner {
        private int inner_a = 11;
        private int inner_b = 11;

        public Inner(int inner_a, int inner_b) {
            System.out.println("Inner Class's Constructor called.");
            System.out.println();
            this.inner_a = inner_a;
            this.inner_b = inner_b;
        }

        void printOuterMember() {
            System.out.println("Outer.this = " + Outer.this);   // 외부 클래스의 인스턴스에 대한 참조정보가 존재함.
            System.out.println("outer_a = " + outer_a);
            System.out.println("outer_b = " + outer_b);
            System.out.println("Outer.this.outer_a = " + Outer.this.outer_a);
            System.out.println("Outer.this.outer_b = " + Outer.this.outer_b);
        }
    }
}
