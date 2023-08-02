import java.sql.Timestamp;
import java.util.Date;

public class DateMain {

    public static void main(String[] args) {
        Date date = new Date(10000);
        Timestamp timestamp = new Timestamp(10000);

        System.out.println(date.equals(timestamp)); // true
        System.out.println(timestamp.equals(date)); // false
    }
}

