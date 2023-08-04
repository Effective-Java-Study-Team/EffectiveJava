package CoRaveler.Item14;

public class IntegerCompareTest {
    public static void main(String[] args) {
        int max = Integer.MAX_VALUE;
        int min = Integer.MIN_VALUE;
        System.out.println("max = " + max);
        System.out.println("min = " + min);

        System.out.println("(max - min) = " + (max - min)); // 오버플로가 나서 '-1' 이 나온다.
        System.out.println("Math.subtractExact(max, min) = " + Math.subtractExact(max, min));   // 정확히는 아래처럼 해야 한다.
    }
}
