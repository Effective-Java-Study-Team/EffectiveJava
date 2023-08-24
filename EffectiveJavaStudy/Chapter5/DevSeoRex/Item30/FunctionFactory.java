import java.util.function.UnaryOperator;

public class FunctionFactory {

    private FunctionFactory() {}

    private static UnaryOperator<Object> IDENTITY_FN = t -> t;

    @SuppressWarnings("unchecked")
    public static <T> UnaryOperator<T> identityFunction() {
        return (UnaryOperator<T>) IDENTITY_FN;
    }
}
