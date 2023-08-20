package mixin;

public class Eagle implements Flyable, Walkable {
    /*
    *   독수리는 날고, 걷는 것이 모두 가능하기 때문에
    *   동작을 정의한 인터페이스를 여러개 구현하여 유연하게 설계할 수 있다.
    * */
    @Override
    public void fly() {
        System.out.println("mixin.Eagle is Fly");
    }

    @Override
    public void walk() {
        System.out.println("mixin.Eagle is Walk");
    }
}
