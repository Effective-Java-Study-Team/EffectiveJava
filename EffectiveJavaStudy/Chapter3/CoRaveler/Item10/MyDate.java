package CoRaveler.Item10;

import java.util.Calendar;
import java.util.Date;

public class MyDate {
    private int year, month, day;

    MyDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MyDate)) {
            // obj가 Date 인스턴스이고 년, 월, 일이 일치하는지 확인합니다.
            if (obj instanceof Date) {
                Calendar cal = Calendar.getInstance();
                cal.setTime((Date) obj);
                return cal.get(Calendar.YEAR) == this.year
                        && cal.get(Calendar.MONTH) + 1 == this.month
                        && cal.get(Calendar.DAY_OF_MONTH) == this.day;
            }
            return false;
        }
        // obj가 MyDate 인스턴스이면, 년, 월, 일만 확인합니다.
        MyDate other = (MyDate) obj;
        return year == other.year && month == other.month && day == other.day;
    }
    // 그러나 이 equals 메서드는 대칭성 원칙을 위반합니다.
    // 즉, 같은 날짜를 가진 MyDate 인스턴스 md과 Date 인스턴스 d에 대해,
    // md.equals(d)는 true를 반환할 수 있지만, d.equals(m)는 false를 반환합니다.
    // 왜냐하면, Date 클래스의 equals 메서드는 시간까지 고려하기 때문에,
    // 날짜는 같지만 시간이 다를 수 있습니다.
}
