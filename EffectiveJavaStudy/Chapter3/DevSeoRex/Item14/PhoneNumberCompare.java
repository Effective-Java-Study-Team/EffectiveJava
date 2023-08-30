package DevSeoRex.Item14;

import java.util.ArrayList;
import java.util.Comparator;

public class PhoneNumberCompare implements Comparable<PhoneNumberCompare> {

    private short areaCode;
    private short prefix;
    private short lineNum;

    // 메서드 연쇄 방식의 정적 비교자 생성 메서드를 이용한 방법
    private static final Comparator<PhoneNumberCompare> COMPARATOR =
            Comparator.comparing((PhoneNumberCompare pn) -> pn.areaCode)
                    .thenComparingInt(pn -> pn.prefix)
                    .thenComparingInt(pn -> pn.lineNum);

    public PhoneNumberCompare(short areaCode, short prefix, short lineNum) {
        this.areaCode = areaCode;
        this.prefix = prefix;
        this.lineNum = lineNum;
    }

//    @Override
//    public int compareTo(PhoneNumberCompare pn) {
//        // 중요한 필드 순서대로 비교를 수행한다.
//        int result = Short.compare(areaCode, pn.areaCode);
//
//        if (result == 0) result = Short.compare(prefix, pn.prefix);
//        if (result == 0) result = Short.compare(lineNum, pn.lineNum);
//
//        return result;
//    }

    @Override
    public int compareTo(PhoneNumberCompare pn) {

        return COMPARATOR.compare(this, pn);
    }
}
