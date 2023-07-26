package ch2.item3;

import java.lang.reflect.Constructor;

public class EnumMain {

    public static void main(String[] args) throws Exception {

        // 열거형은 인스턴스화 불가능하다.
//        EnumElvis elvis = new EnumElvis();

        // 열거타입으로 싱글턴을 보장받을 수 있다.
        EnumElvis elvis2 = EnumElvis.INSTANCE;

        Constructor<EnumElvis> constructor = EnumElvis.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        EnumElvis reflectElvis = constructor.newInstance();

    }
}
