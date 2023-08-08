package item2.builder;


/**
* 빌더 패턴
 * 점층적 생성자 패턴과 자바빈즈 패턴의 장점만 취함
**/
public class Company {
    private final String name;
    private final String address;
    private final int businessNumber;
    private final String industry;
    private final int employeeCount;
    private final String phoneNumber;

    public static class Builder{
        //필수
        private final String name;
        private final String address;
        private final int businessNumber;

        //선택 - 기본값 초기화
        private String industry = "NONE";
        private int employeeCount = 0;
        private String phoneNumber = "NONE";

        public Builder(String name, String address, int businessNumber){
            this.name = name;
            this.address = address;
            this.businessNumber = businessNumber;
        }

        public Builder industry(String val){
            industry = val;
            return this;
        }

        public Builder employeeCount(int val){
            employeeCount = val;
            return this;
        }

        public Builder phoneNumber(String val){
            phoneNumber = val;
            return this;
        }

        public Company build(){
            return new Company(this);
        }
    }

    private Company(Builder builder){
        name = builder.name;
        address = builder.address;
        businessNumber = builder.businessNumber;
        industry = builder.industry;
        employeeCount = builder.employeeCount;
        phoneNumber = builder.phoneNumber;
    }

    public Company(String name, String address, int businessNumber, String industry, int employeeCount, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.businessNumber = businessNumber;
        this.industry = industry;
        this.employeeCount = employeeCount;
        this.phoneNumber = phoneNumber;
    }
}
