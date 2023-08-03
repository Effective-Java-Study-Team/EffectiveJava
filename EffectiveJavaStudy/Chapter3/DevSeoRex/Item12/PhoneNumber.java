public class PhoneNumber {

    private String areaNum;
    private String prefix;
    private String lineNum;

    public PhoneNumber(String areaNum, String prefix, String lineNum) {
        this.areaNum = areaNum;
        this.prefix = prefix;
        this.lineNum = lineNum;
    }

    /*
    *   이 전화번호의 문자열 표현을 반환한다.
    *   XXX-YYY-ZZZZ 형태의 12글자로 구성된다.
    *
    * */
    @Override
    public String toString() {
        return areaNum + "-" + prefix + "-" + lineNum;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (!(obj instanceof PhoneNumber phoneNumber)) return false;

        return areaNum.equals(phoneNumber.areaNum) && prefix.equals(phoneNumber.prefix)
                && lineNum.equals(phoneNumber.lineNum);
    }
}


