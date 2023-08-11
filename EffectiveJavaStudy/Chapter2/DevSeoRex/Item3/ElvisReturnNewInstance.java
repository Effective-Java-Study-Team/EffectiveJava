package ch2.Item3;

public class ElvisReturnNewInstance {


    // 객체를 인스턴스화 하지 못하도록 방지
    private ElvisReturnNewInstance() {}

    // 메서드의 시그니처 변경(API 수정)없이 요청마다 새로운 객체를 반환할 수 있도록 유연한 대처가 가능하다.
    public static ElvisReturnNewInstance getInstance() {
        return new ElvisReturnNewInstance();
    }

    public void leaveTheBuilding() {}
}
