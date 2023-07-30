package ArrayProblem;

import java.util.Objects;

public class SubClassAttackMain {

    public static void main(String[] args) throws InterruptedException {

        try {
            new SubClass(-1);
        } catch (IllegalStateException e) {
            System.out.println(e);
        }

        System.gc();
        System.runFinalization();

        Thread.sleep(1000);
        System.out.println();

        if (Objects.isNull(SubClass.superClass)) {
            System.out.println("attack failed");
        } else {
            System.out.println("attack success");
        }

    }


}


