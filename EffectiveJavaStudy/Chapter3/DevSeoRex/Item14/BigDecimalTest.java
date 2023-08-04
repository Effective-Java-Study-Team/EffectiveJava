import java.math.BigDecimal;

public class BigDecimalTest {

    public static void main(String[] args) {
        BigDecimal decimal1 = new BigDecimal("1.0");
        BigDecimal decimal2 = new BigDecimal("1.00");

        PhoneNumberCompare phoneNumberCompare = new PhoneNumberCompare((short) 1, (short) 1, (short) 1);
        System.out.println(phoneNumberCompare);

        // equals와 compareTo의 결과가 다르다 -> 정렬된 컬렉션을 쓸때 문제가 생길 수 있다.
        System.out.println(decimal1.equals(decimal2));
        System.out.println(decimal1.compareTo(decimal2));

    }
}
