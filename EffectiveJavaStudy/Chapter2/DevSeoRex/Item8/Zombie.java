package ArrayProblem;

public class Zombie {

    static Zombie zombie;
    int value;

    public Zombie(int value) {
        super();
        if (value < 0) {
            System.out.println("constructor this = " + this);
            throw new IllegalArgumentException("Negative Zombie value");
        }
        this.value = value;
    }

    @Override
    public void finalize() {
        System.out.println("호출");
        System.out.println("this = " + this);
        zombie = this;
    }
}


