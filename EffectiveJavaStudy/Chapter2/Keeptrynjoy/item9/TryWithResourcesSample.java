package ch2.item9;

import java.io.*;

public class TryWithResourcesSample {

    private static final byte BUFFER_SIZE = 31;

    static String firstLineOfFile(String path) throws IOException{

        try(BufferedReader br = new BufferedReader(
                new FileReader(path))){
            return br.readLine(); // close에서 발생한 예외는 숨겨지고 leadLine에서 발생한 예외가 기록된다.
        }
    }

    static void copy(String src, String dst) throws IOException{
        try (InputStream in = new FileInputStream(src);
             OutputStream out = new FileOutputStream(dst)){

            byte[] buf = new byte[BUFFER_SIZE];
            int n;
            while ((n = in.read(buf)) >= 0)
                out.write(buf, 0, n);
        }
    }
}
