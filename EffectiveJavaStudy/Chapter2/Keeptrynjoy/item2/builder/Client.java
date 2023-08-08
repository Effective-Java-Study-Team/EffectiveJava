package Chapter2.Keeptrynjoy.item2.builder;

public class Client {
    public void clientMethod(){
        Company naver = new Company.Builder("NAVER","경기도",110000000)
                .industry("IT")
                .employeeCount(30000)
                .phoneNumber("05112341245")
                .build();
    }
}
