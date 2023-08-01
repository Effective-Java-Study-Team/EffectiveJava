package CH2.Item9;

import java.io.*;
import java.util.Arrays;

public class tryFinallyBothError {
    public static void main(String[] args) throws Exception {
        try (FaultyBufferedReader br = new FaultyBufferedReader(new InputStreamReader(System.in))) {
            System.out.println(br.readLine());
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("e.getSuppressed() = " + Arrays.toString(e.getSuppressed()));
        }
//        finally {
//            br.close();   // 이건 안됨, try-with-resources 문법에 없다.
//        }
    }

    static class FaultyBufferedReader extends BufferedReader {
        public FaultyBufferedReader(Reader in) {
            super(in);
        }

        @Override
        public String readLine() throws IOException {
            throw new IOException("IOException during readLine()");
        }

        @Override
        public void close() throws IOException {
            throw new IOException("IOException during close()");
        }
    }
}