package item2.javabeens;

/**
* 자바빈즈 패턴
 * 객체가 완전히 생성되기 전까지는 일관성(consistency)이 무너진 상태에 놓이게 됨.
 * 불변 객체로 만들 수 없음
**/
public class Company {
    private String name = null;
    private String address = null;
    private int businessNumber = -1;
    private String industry = "NONE";
    private int employeeCount = 0;
    private String phoneNumber = "NONE";

    public Company() {
    }

    public Company(String name, String address, int businessNumber, String industry, int employeeCount, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.businessNumber = businessNumber;
        this.industry = industry;
        this.employeeCount = employeeCount;
        this.phoneNumber = phoneNumber;
    }

    public void setName(String val){
        name = val;
    }

    public void setAddress(String val){
        address = val;
    }

    public void setBusinessNumber(int val){
        businessNumber = val;
    }

    public void setIndustry(String val){
        industry = val;
    }

    public void setEmployeeCount(int val){
        employeeCount = val;
    }

    public void setPhoneNumber(String val){
        phoneNumber = val;
    }
}
