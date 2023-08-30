package CoRaveler.Item32;

import java.util.concurrent.ThreadLocalRandom;

public class PickTwo {
    static <T> T[] toArray(T... args) {
        System.out.println("args.getClass() = " + args.getClass());
        return args;
    }

    static <T> T[] pickTwo(T a, T b, T c) {
        System.out.println("a.getClass() = " + a.getClass());
        switch (ThreadLocalRandom.current().nextInt(3)) {
            case 0:
                return toArray(a, b);
            case 1:
                return toArray(a, c);
            case 2:
                return toArray(b, c);
        }
        throw new AssertionError();
    }

    public static void main(String[] args) {
        System.out.println();
        String[] attrs = PickTwo.pickTwo("좋은", "빠른", "저렴한");
    }
}
