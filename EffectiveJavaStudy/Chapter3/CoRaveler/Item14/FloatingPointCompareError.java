package CoRaveler.Item14;

public class FloatingPointCompareError {
    public static void main(String[] args) {
        double a = 0.0 / 0.0;
        int aHashCode = Double.hashCode(a);
        double b = 0.0 / 0.0;
        int bHashCode = Double.hashCode(b);
        System.out.println("(a == b) = " + (a == b));
        System.out.println("(aHashCode-bHashCode) = " + (aHashCode - bHashCode));
        System.out.println("Double.compare(aHashCode, bHashCode) = " + Double.compare(aHashCode, bHashCode));
        System.out.println();

        double posZero = 0.0;
        int posZeroHashCode = Double.hashCode(posZero);
        double negZero = -0.0;
        int negZeroHashCode = Double.hashCode(negZero);
        System.out.println("(posZero == negZero) = " + (posZero == negZero));
        System.out.println("(posZeroHashCode == negZeroHashCode) = " + (posZeroHashCode == negZeroHashCode));
        System.out.println("Double.compare(posZeroHashCode, negZeroHashCode) = " + Double.compare(posZeroHashCode, negZeroHashCode));
    }
}
