package ArrayProblem;

import java.io.IOException;

public class FakeReader implements AutoCloseable {


    public void readLine(String s) {
        Integer.parseInt(s);
    }

    @Override
    public void close()  {
        throw new RuntimeException(new IOException("닫는 중 예외 발생!"));
    }
}



