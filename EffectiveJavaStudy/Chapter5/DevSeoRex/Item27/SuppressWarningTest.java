import java.lang.reflect.Array;

public class SuppressWarningTest {

    public static void main(String[] args) {

    }


    private static <T,U> T[] copyOfRange(U[] original, int from, int to, Class<? extends T[]> newType) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);

        // T[ ] 타입의 하위 타입만 메서드에서 사용되므로 타입 안전함이 보장되므로 경고를 제거한다.
        @SuppressWarnings("unchecked")
        T[] copy = ((Object)newType == (Object) Object[].class)
                ? (T[]) new Object[newLength]
                : (T[]) Array.newInstance(newType.getComponentType(), newLength);
        System.arraycopy(original, from, copy, 0,
                Math.min(original.length - from, newLength));
        return copy;
    }
}
