package CoRaveler.Item34;

import java.util.function.BiFunction;
import java.util.function.DoubleBinaryOperator;

public enum LambdaOperation {
    PLUS((x, y) -> x + y),  // PLUS(DOUBLE::sum)
    MINUS((x, y) -> x - y),
    TIMES((x, y) -> x * y),
    DIVIDE((x, y) -> x / y);

    private final DoubleBinaryOperator func;

    LambdaOperation(DoubleBinaryOperator func) {
        this.func = func;
    }

    public double calc(int x, int y) {
        return func.applyAsDouble(x, y);
    }
}
