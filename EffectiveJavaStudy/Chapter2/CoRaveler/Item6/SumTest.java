package CH2.Item6;

public class SumTest {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        System.out.println("sum1() = " + sum1());
        long end = System.currentTimeMillis();
        System.out.println("sum1() took " + (end - start) + "ms");

        start = System.currentTimeMillis();
        System.out.println("sum2() = " + sum2());
        end = System.currentTimeMillis();
        System.out.println("sum2() took " + (end - start) + "ms");
    }

    private static long sum1() {
        Long sum = 0L;
        for (long i = 0; i <= Integer.MAX_VALUE; i++) {
            sum += i;
        }
        return sum;
    }

    private static long sum2() {
        long sum = 0L;
        for (long i = 0; i <= Integer.MAX_VALUE; i++) {
            sum += i;
        }
        return sum;
    }
}
