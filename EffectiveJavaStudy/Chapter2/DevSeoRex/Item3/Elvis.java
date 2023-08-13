package ch2.Item3;

public class Elvis {

    public static final Elvis INSTANCE = new Elvis();

    // 객체를 인스턴스화 하지 못하도록 방지
    private Elvis() {}

    public static Elvis getInstance() {return INSTANCE;}

    public void leaveTheBuilding() {}
}
