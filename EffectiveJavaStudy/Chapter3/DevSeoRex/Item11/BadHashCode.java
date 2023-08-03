public class BadHashCode {

    private int x;
    private int y;

    public BadHashCode(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (!(obj instanceof BadHashCode badHashCode)) return false;

        return x == badHashCode.x && y == badHashCode.y;
    }

    @Override
    public int hashCode() {
        return 45;
    }
}


