import java.util.Objects;

public final class CaseInsensitiveString {

    private final String s;

    public CaseInsensitiveString(String s) {
        this.s = Objects.requireNonNull(s);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof  CaseInsensitiveString) {
            return s.equalsIgnoreCase(((CaseInsensitiveString) o).s);
        }

        if (o instanceof String) { // 한 방향으로만 적용된다.
            return s.equalsIgnoreCase((String) o);
        }

        return false;
    }
}
