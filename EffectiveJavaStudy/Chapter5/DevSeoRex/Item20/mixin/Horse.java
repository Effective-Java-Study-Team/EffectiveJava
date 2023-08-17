package mixin;

public class Horse implements Walkable {
    /*
    *   말 클래스는 걷는 것만 가능하기 때문에 걷는 동작만 정의한다.
    *   mixin.Animal 클래스를 상속받았다면 나는 동작이 없음에도 강제로 상속받아야 하는 문제가 생긴다.
    * */
    @Override
    public void walk() {
        System.out.println("mixin.Horse is walk");
    }
}
