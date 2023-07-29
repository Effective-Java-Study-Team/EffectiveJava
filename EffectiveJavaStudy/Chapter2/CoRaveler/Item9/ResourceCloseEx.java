package CH2.Item9;

import java.io.*;

public class ResourceCloseEx {
    static int BUFFER_SIZE = 10;

    public static void main(String[] args) {

    }

    // 1. 전통 방식인 try-finally
    static String firstLineOfFile(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        try {
            return br.readLine();
        } finally {
            br.close();
        }
    }

    // 2. 자원을 회수해야 하는 경우가 2가지 이상인 경우, try-finally 2 중첩의 경우
    static void copy(String src, String dest) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dest);
            try {
                byte[] buf = new byte[BUFFER_SIZE];
                int n;
                while ((n = in.read(buf)) >= 0) {
                    out.write(buf, 0, n);
                }
            } finally {
                out.close();                                // out 해체
            }
        } finally {
            in.close();                                     // in 해제
        }
    }

    // 3. 1번 방식을 try-with-resources 로 수정
    static String firstLineOfFile2(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return br.readLine();
        }
    }

    // 4. 2번 방식을 try-with-resources 로 수정
    static void copy2(String src, String dest) throws IOException {
        try (InputStream in = new FileInputStream(src);
             OutputStream out = new FileOutputStream(dest)) {
            byte[] buf = new byte[BUFFER_SIZE];
            int n;
            while((n = in.read(buf)) >= 0) {
                out.write(buf, 0, n);
            }
        }
    }
}
