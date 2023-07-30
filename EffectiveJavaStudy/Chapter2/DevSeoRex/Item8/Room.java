package ArrayProblem;

import java.lang.ref.Cleaner;

public class Room implements AutoCloseable {
    private static final Cleaner cleaner = Cleaner.create();

    private static class State implements Runnable {
        int numJunkPiles;

        State(int numJunkPiles) {
            this.numJunkPiles = numJunkPiles;
        }

        @Override
        public void run() {
            System.out.println("방 청소");
            numJunkPiles = 0;
        }
    }

    private final State state;

    private final Cleaner.Cleanable cleanable;

    public Room(int numJunkPile) {
        state = new State(numJunkPile);
        cleanable = cleaner.register(this, state);
    }

    @Override
    public void close() {
        System.out.println("호출");
        try {
            throwException();
        } catch (RuntimeException e) {
            System.out.println("clean 호출 중 예외 발생");
        }

        cleanable.clean();
        System.out.println("호출 완료");
    }

    private void throwException() {
        throw new RuntimeException("예외가 발생했다!");
    }
}
