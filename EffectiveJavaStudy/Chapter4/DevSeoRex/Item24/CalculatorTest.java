public class CalculatorTest {

    public static void main(String[] args) {

        double plus = Calculator.Operation.PLUS.apply(2, 3);
        double minus = Calculator.Operation.MINUS.apply(2, 3);
        double multiply = Calculator.Operation.TIMES.apply(2, 3);
        double divide = Calculator.Operation.DIVIDE.apply(2, 3);

        System.out.println("plus = " + plus);
        System.out.println("minus = " + minus);
        System.out.println("multiply = " + multiply);
        System.out.println("divide = " + divide);
    }
}
