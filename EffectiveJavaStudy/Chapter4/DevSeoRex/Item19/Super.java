public class Super {

    public Super() {
        // 상속용 클래스는 재정의 가능한 메서드를 생성자내에서 호출해서는 안된다.
        overrideMe();
    }

    public void overrideMe() {
    }

}
