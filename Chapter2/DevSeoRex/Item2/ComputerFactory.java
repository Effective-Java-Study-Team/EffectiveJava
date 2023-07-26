package ch2.item2;

public class ComputerFactory {

    // 필수 매개변수
    private String factoryName;
    private String owner;
    private int computerQuantity;

    // 선택 매개변수
    private int moneyLeft;
    private int clerkCount;

    public ComputerFactory(String factoryName, String owner, int computerQuantity, int moneyLeft, int clerkCount) {
        this.factoryName = factoryName;
        this.owner = owner;
        this.computerQuantity = computerQuantity;
        this.moneyLeft = moneyLeft;
        this.clerkCount = clerkCount;
    }

    public static class FactoryBuilder {

        // 필수 매개변수
        private final String factoryName;
        private final String owner;
        private final int computerQuantity;

        // 선택 매개변수 - 기본값으로 초기화 해준다.
        private int moneyLeft = 0;
        private int clerkCount = 0;

        public FactoryBuilder(String factoryName, String owner, int computerQuantity) {
            this.factoryName = factoryName;
            this.owner = owner;
            this.computerQuantity = computerQuantity;
        }

        public FactoryBuilder moneyLeft(int moneyLeft) {
            if (moneyLeft < 0) throw new IllegalArgumentException("잔고는 0원보다 낮을 수 없습니다.");
            this.moneyLeft = moneyLeft;
            return this;
        }



        public FactoryBuilder clerkCount(int clerkCount) {
            this.clerkCount = clerkCount;
            return this;
        }

        public ComputerFactory build() {
            return new ComputerFactory(factoryName, owner, computerQuantity, moneyLeft, clerkCount);
        }
    }
}
