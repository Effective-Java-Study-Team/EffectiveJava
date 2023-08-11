import java.util.Properties;

public class InheritanceProblem {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("1", 2);

        System.out.println(properties.getProperty("1"));
        System.out.println(properties.get("1"));


    }
}
