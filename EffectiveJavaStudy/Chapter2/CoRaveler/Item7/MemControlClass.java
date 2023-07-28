package CH2.Item7;

public class MemControlClass {
    public static void main(String[] args) {

    }
}

class MemMine {
    private static int[] els = {1, 2, 3, 4};

    private MemMine(){}

    public int[] FactoryMemMine() {
        return els;
    }
}