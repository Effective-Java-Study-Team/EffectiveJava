import java.util.function.UnaryOperator;

public class FunctionTest {

    public static void main(String[] args) {
        String[] strings = { "삼베", "대마", "나일론" };
        UnaryOperator<String> sameString = FunctionFactory.identityFunction();

        for (String s : strings) {
            System.out.println(sameString.apply(s));
        }

        Number[] numbers = { 1, 2.0f, 3L };
        UnaryOperator<Number> sameNumber = FunctionFactory.identityFunction();
        for (Number n : numbers) {
            System.out.println(sameNumber.apply(n));
        }
    }
}
