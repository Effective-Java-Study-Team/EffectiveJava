package ch2.item2;

import static ch2.item2.SamsungPhone.Kind.*;
import static ch2.item2.SmartPhone.Option.*;
import static ch2.item2.IPhone.CameraCount.*;

public class PhoneMain {

    public static void main(String[] args) {
        // self 메서드가 오버라이딩 되어 있기 때문에 유연하게 하위타입을 반환해준다.
        SamsungPhone samsungPhone = new SamsungPhone.Builder(PREMIUM)
                .addOption(BLUETOOTH)
                .addOption(EARPHONE)
                .build();

        IPhone iPhone = new IPhone.Builder(THREE)
                .addOption(DMB)
                .addOption(WIFI)
                .build();
    }
}
