import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class C implements Serializable {

    int x;
    int y;

    public C(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        inputStream.defaultReadObject();
        overrideMe();
    }

    protected Object writeReplace() {
        System.out.println("writeReplace 호출!");
        return null;
    }


    protected Object readResolve() {
        System.out.println("readResolve 호출!");
        return null;
    }

    public void overrideMe() {}
}
