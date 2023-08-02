import java.net.MalformedURLException;
import java.net.URL;

public class URLMain {

    public static void main(String[] args) throws MalformedURLException {
        URL url1 = new URL("https://www.naver.com");
        URL url2 = new URL("https://www.naver.com");


        for (int i=0; i<10000000; i++) {
            System.out.println(url1.equals(url2));
        }
    }
}
