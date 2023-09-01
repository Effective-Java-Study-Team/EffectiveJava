package CoRaveler.Item34;

import static CoRaveler.Item34.HolidayIncludedPayrollDay.PayType.WEEKDAY;
import static CoRaveler.Item34.HolidayIncludedPayrollDay.PayType.WEEKEND;

public enum HolidayIncludedPayrollDay {

    MONDAY(WEEKDAY), TUESDAY(WEEKDAY), WEDNESDAY(WEEKDAY), THURSDAY(WEEKDAY), FRIDAY(WEEKDAY),
    SATURDAY(WEEKEND), SUNDAY(WEEKEND);

    private final PayType payType;

    HolidayIncludedPayrollDay(PayType payType) {
        this.payType = payType;
    }

    int pay(int minutesWorked, int payRate, boolean isHoliday) {
        PayType currPayType = isHoliday ? PayType.HOLIDAY : payType;
        return currPayType.pay(minutesWorked, payRate);
    }

    enum PayType {
        WEEKDAY {
            int overtimePay(int minsWorked, int payRate) {
                return minsWorked <= MINS_PER_SHIFT ? 0 : (minsWorked - MINS_PER_SHIFT) * payRate / 2;
            }
        },
        WEEKEND {
            int overtimePay(int minsWorked, int payRate) {
                return minsWorked * payRate / 2;
            }
        },
        HOLIDAY {
            int overtimePay(int minsWorked, int payRate) {
                return minsWorked * payRate * 10;
            }
        };

        private static final int MINS_PER_SHIFT = 8 * 60;

        abstract int overtimePay(int mins, int payRate);

        int pay(int minsWorked, int payRate) {
            int basePay = minsWorked * payRate;
            return basePay + overtimePay(minsWorked, payRate);
        }
    }
}
