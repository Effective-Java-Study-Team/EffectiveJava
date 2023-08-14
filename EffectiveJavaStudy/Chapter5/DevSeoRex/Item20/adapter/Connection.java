package adapter;

import java.util.Objects;

public interface Connection {

    static void connect(Volt220 volt220) {
        Objects.requireNonNull(volt220);

        System.out.println(volt220.volt + " 연결 성공");
    }
}
