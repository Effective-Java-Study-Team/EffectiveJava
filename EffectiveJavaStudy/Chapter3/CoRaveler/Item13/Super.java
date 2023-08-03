package CoRaveler.Item13;

public class Super implements Cloneable{
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    void overrideMe(){}
}
