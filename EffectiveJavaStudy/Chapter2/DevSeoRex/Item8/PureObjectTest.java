package ch2.item8;

public class PureObjectTest {

    public static void main(String[] args) throws Throwable {
        PureObject object = new PureObject();
        object = null;

        System.gc();
    }
}

