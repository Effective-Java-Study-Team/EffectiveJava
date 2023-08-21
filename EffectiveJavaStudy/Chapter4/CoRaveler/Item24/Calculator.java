package CoRaveler.Item24;

public class Calculator {
    static private int pri1 = 1;
    private int pri2 = 2;

    static class Operation {
        public static final String PLUS = "+";
        public static final String MINUS = "-";

        void print() {
            System.out.println(pri1);
        }
    }

    public int calculate(int a, int b, String operation) {
        if (Operation.PLUS.equals(operation)) {
            return a + b;
        } else if (Operation.MINUS.equals(operation)) {
            return a - b;
        } else {
            throw new AssertionError(operation);
        }
    }
}