package CoRaveler.Item1;

public class equalsOverridingTest {
    public static void main(String[] args) {
        Object x = new Test();
        x.equals("abc");
    }
}

class Test {
    public Test() {
    }

    @Override
    public boolean equals(Object obj) {
        System.out.println("equals of Test");
        return true;
    }
}