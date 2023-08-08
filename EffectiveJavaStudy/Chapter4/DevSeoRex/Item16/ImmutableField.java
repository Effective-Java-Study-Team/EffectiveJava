public class ImmutableField {

    private static final int HOURS_PER_DAY = 24;
    private static final int MINUTES_PER_HOUR = 60;

    public final int hour;
    public final int minutes;

    public ImmutableField(int hour, int minutes) {
        this.hour = hour;
        this.minutes = minutes;
    }
}
