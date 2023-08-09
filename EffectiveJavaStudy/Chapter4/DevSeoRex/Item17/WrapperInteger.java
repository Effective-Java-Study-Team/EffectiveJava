

public class WrapperInteger {

    private int number;

    public WrapperInteger(int number) {
        this.number = number;
    }
    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WrapperInteger that)) return false;
        return number == that.number;
    }

    @Override
    public int hashCode() {
        return 31 * Integer.hashCode(number);
    }

    @Override
    public String toString() {
        return "WrapperInteger{" +
                "number=" + number +
                '}';
    }
}
