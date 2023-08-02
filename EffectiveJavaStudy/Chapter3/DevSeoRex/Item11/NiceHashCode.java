public class NiceHashCode {

    private int x;
    private int y;

    public NiceHashCode(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (!(obj instanceof NiceHashCode niceHashCode)) return false;

        return x == niceHashCode.x && y == niceHashCode.y;
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(x);
        result = 31 * result + Integer.hashCode(y);
        return result;
    }
}
