package ch2.item3;

import java.io.Serializable;

public class MyInt implements Serializable {

    private static final MyInt INSTANCE = new MyInt(6);

    private transient int number;

    private MyInt() {}

    public static MyInt getInstance() {
        return INSTANCE;
    }
    private MyInt(int number) {
        this.number = number;
    }



    private Object readResolve() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return "MyInt{" +
                "number=" + number +
                '}';
    }
}
