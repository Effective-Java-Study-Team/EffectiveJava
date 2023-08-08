public class Encapsulation {

    private int x;
    private int y;

    // 접근자와 변경자 메서드를 활용해 데이터를 캡슐화한 개선 클래스
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
