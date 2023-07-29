package CH2.Item9;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] args) throws Exception {
        String path = "";
        InputStream in = new FileInputStream(path);
        OutputStream out = new FileOutputStream(path);
        FileReader file = new FileReader(path);


        Connection conn = null;
        // JDBC 드라이버 로딩
        Class.forName("com.mysql.jdbc.Driver");

        // 데이터베이스에 연결
        String url = "";
        String user = "user";
        String pwd = "pwd";
        conn = DriverManager.getConnection(url, user, pwd);
    }
}
