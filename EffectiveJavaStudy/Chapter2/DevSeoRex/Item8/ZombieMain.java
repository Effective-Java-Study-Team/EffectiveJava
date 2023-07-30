package ArrayProblem;

public class ZombieMain {

    public static void main(String[] args) throws InterruptedException {
        Zombie zombie = null;
        try {
            zombie = new Zombie(-1);

        } catch (IllegalArgumentException e) {
            System.out.println(zombie);
            System.out.println(e);
        }

        System.gc();
        System.runFinalization();

        Thread.sleep(1000);
        System.out.println();

        System.out.println(Zombie.zombie);
        System.out.println(Zombie.zombie.value);

    }
}




