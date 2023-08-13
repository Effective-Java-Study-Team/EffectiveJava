package CoRaveler.Item19;

import java.time.Instant;

public class SuperSubOverride {
    public static void main(String[] args) {
        Sub sub = new Sub();
        sub.overrideMe();
    }
}

class Super {
    public Super() {
        System.out.println("Super()");
        overrideMe();
    }

    public void overrideMe(){
        System.out.println("Super's overrideMe()");
    }
}

final class Sub extends Super {
    private final Instant instant;

    Sub() {
        System.out.println("Sub()");
        instant = Instant.now();
    }

    @Override
    public void overrideMe() {
        System.out.println("Sub's overrideMe()");
        System.out.println(instant);
    }
}