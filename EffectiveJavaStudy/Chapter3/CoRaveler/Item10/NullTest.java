package CoRaveler.Item10;

import java.util.Date;
import java.util.Objects;

public class NullTest {
    public static void main(String[] args) {
        Date d = new Date();
        Object o = new Object();
        System.out.println("o.equals(null) = " + o.equals(null));

        System.out.println("(Objects.equals(null, o)) = " + (Objects.equals(null, o)));
    }
}
