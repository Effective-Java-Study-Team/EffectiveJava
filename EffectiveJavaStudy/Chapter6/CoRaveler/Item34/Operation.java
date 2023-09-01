package CoRaveler.Item34;

import java.lang.Enum;

public enum Operation {
    PLUS {
        public double apply(int x, int y) {
            return x + y;
        }
    }, MINUS {
        public double apply(int x, int y) {
            return Math.abs(x - y);
        }
    }, TIMES {
        public double apply(int x, int y) {
            return x * y;
        }
    }, DIVIDE {
        public double apply(int x, int y) {
            return (double) x / y;
        }
    };

    public abstract double apply(int x, int y);
}
