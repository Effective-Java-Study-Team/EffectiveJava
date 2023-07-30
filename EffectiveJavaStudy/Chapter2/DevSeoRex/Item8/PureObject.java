package ch2.item8;


public class PureObject {

    private static PureObject object;


    @Override
    protected void finalize() throws Throwable {
        object = new PureObject();
    }



}


