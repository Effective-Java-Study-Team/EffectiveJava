package CoRaveler.Item18;

import java.io.FileOutputStream;
import java.util.Properties;

public class PropertiesStoreError {
    public static void main(String[] args) {
        Properties p = new Properties();
        p.setProperty("ID", "park");
        p.setProperty("PWD", "1234");
        p.setProperty("TEL", "010-0000-0000");
        System.out.println("p = " + p);

        p.put("PWD2", 1234);
        System.out.println("p = " + p); // Integer 를 value 로 저장해도 잘 출력이 된다.

        try(FileOutputStream outputStream = new FileOutputStream("config.properties")){
            p.store(outputStream, "This is 주석");
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }
}
