public class CanonicalString {

    private String s;
    private String canonical;

    public CanonicalString(String s) {
        this.s = s;
        this.canonical = s.replaceAll(" ", "");
    }

    public void setString(String s) {
        this.s = s;
        this.canonical = s.replaceAll(" ", "");
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;

        if (!(o instanceof CanonicalString)) return false;

        CanonicalString canonicalString = (CanonicalString) o;

        return canonicalString.s.equals(canonical);
    }


}
