import java.math.BigInteger;

public class OverrideToString {

    public static void main(String[] args) {
        PhoneNumber phoneNumber1 = new PhoneNumber("010", "1111", "1112");
        PhoneNumber phoneNumber2 = new PhoneNumber("010", "1111", "1113");

        assertTrue(phoneNumber1, phoneNumber2);
    }

    static void assertTrue(PhoneNumber phoneNumber1, PhoneNumber phoneNumber2) {

        if (phoneNumber1.equals(phoneNumber2)) {
            System.out.println("Assertion Success! ");
        } else {
            System.out.println("Assertion failure : expected " + phoneNumber1 + ", but was " + phoneNumber2 + ".");
        }
    }
}

