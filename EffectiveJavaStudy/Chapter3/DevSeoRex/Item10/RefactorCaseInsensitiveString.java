import java.util.Objects;

public final class RefactorCaseInsensitiveString {

    private final String s;

    public RefactorCaseInsensitiveString(String s) {
        this.s = Objects.requireNonNull(s);
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }


        return o instanceof RefactorCaseInsensitiveString &&
                s.equalsIgnoreCase(((RefactorCaseInsensitiveString) o).s);
    }
}

