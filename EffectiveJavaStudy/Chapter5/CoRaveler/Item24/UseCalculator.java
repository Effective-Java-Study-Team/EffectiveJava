package CoRaveler.Item24;

public class UseCalculator {
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        int result = calculator.calculate(1, 2, Calculator.Operation.PLUS);
        int result2 = calculator.calculate(1, 2, Calculator.Operation.MINUS);
        System.out.println("result = " + result);
        System.out.println("result2 = " + result2);
    }
}
