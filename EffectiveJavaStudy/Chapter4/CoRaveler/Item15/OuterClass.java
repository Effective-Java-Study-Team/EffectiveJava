package CoRaveler.Item15;

public class OuterClass {
    private static class NestedClass {
        void doSomething() {
            System.out.println("do someThing in NestedClass activated");
        }
    }

    public static void main(String[] args) {
        TopLevelClass tlc = new TopLevelClass();
        NestedClass nc = new NestedClass();
        tlc.doSomething();
        nc.doSomething();
    }
}
