package DevSeoRex.Item34;

public enum Operation {

    PLUS, MINUS, TIMES, DIVIDE;

    public double apply(double x, double y) {
        switch (this) {
            case PLUS : return x + y;
            case MINUS : return x - y;
            case TIMES : return x * y;
            case DIVIDE : return x / y;
        }
        throw new AssertionError("알 수 없는 연산 : " + this);
    }

    /*
    *  switch 문을 이용한 방법은 기존 열거 타입에 상수별 동작을 혼합해 넣을 때 유용하다.
    *  예를 들어 아래 메서드처럼 각 연산의 반대 연산을 반환하는 메서드가 필요할때는 이 방법이 좋다.
    * */
    public static Operation inverse(Operation op) {
        switch (op) {
            case PLUS : return Operation.MINUS;
            case MINUS : return Operation.PLUS;
            case TIMES : return Operation.DIVIDE;
            case DIVIDE : return Operation.TIMES;

            default : throw new AssertionError("알 수 없는 연산 : " + op);
        }
    }
}
