import java.math.BigDecimal;
import java.math.BigInteger;

public class BigTest {

    public static void main(String[] args) {

        BigDecimal bigDecimal = new BigDecimal(10);
        BigDecimal bigDecimal2 = bigDecimal.add(BigDecimal.valueOf(10));

        BigInteger bigInteger = new BigInteger("10");
        BigInteger bigInteger2 = bigInteger.add(BigInteger.valueOf(20));



        System.out.println(bigDecimal.equals(bigDecimal2)); // false
        System.out.println(bigDecimal.compareTo(bigDecimal2)); // -1
        System.out.println((bigDecimal == bigDecimal2)); // false

        System.out.println(bigInteger.equals(bigInteger2)); // false
        System.out.println(bigInteger.compareTo(bigInteger2)); // -1
        System.out.println((bigInteger == bigInteger2)); // false
    }
}
