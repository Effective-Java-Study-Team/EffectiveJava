
import java.util.Properties;

public class InheritanceProblem {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("1", 2);
        properties.setProperty("2", "4");

        System.out.println(properties.getProperty("1"));  // null
        System.out.println(properties.get("1")); // 2

        System.out.println(properties.get("2"));  // 4
        System.out.println(properties.getProperty("2")); // 4

    }
}
