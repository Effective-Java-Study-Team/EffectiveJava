import java.util.Comparator;

public class HashCodeCompare {

    final int hashCode;

    static Comparator<HashCodeCompare> HASH_COMPARATOR =
            (c1, c2) -> c1.hashCode - c2.hashCode;


    public HashCodeCompare(int hashCode) {
        this.hashCode = hashCode;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        return "HashCodeCompare{" +
                "hashCode=" + hashCode +
                '}';
    }
}
