package mixin;

public abstract class Animal {

    /*
    *   동물이 할 수 있는 모든 동작을 정의한 클래스
    *   -> 소리를 내는 기능
    *   -> 걷는 기능
    *   -> 나는 기능
    * */
    abstract void makeSound(String sound);
    abstract void walk();
    abstract void fly();
}
