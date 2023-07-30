package ArrayProblem;

import java.io.*;

public class ResourceMain {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            Integer.parseInt(br.readLine());
        } finally {
            br.close();
            throw new IOException("두번째 예외 받아라!");
        }

    }

    static String findLineOfFile(String path) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(path));
        try {
            return br.readLine();
        } finally {
            br.close();
        }
    }

    static void copy(String src, String dst) throws IOException {
        InputStream in = new FileInputStream(src);

        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                byte[] buf = new byte[30];
                int n;
                while ((n = in.read(buf)) >= 0)
                    out.write(buf, 0, n);
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }

    }


}
