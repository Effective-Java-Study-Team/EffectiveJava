public class Main {

    public static void main(String[] args) {

        Encapsulation encapsulation = new Encapsulation();
        encapsulation.setX(10);
        encapsulation.setY(10);

        int x1 = encapsulation.getX();
        int y1 = encapsulation.getY();

        // 접근자 & 변경자 메서드를 사용하는 것보다 클라이언트 코드가 훨씬 가독성이 좋다.
        PackagePrivate packagePrivate = new PackagePrivate();
        packagePrivate.x = 10;
        packagePrivate.y = 10;

        // 패키지 안에서만 사용하는 클래스기 때문에 변수를 외부에 노출시킬 필요는 없다.
        int x2 = packagePrivate.x;
        int y2 = packagePrivate.y;
    }
}
