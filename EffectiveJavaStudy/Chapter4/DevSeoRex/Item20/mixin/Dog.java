package mixin;

public class Dog extends Animal {
    @Override
    void makeSound(String sound) {
        System.out.println("DogSound = " + sound);
    }

    @Override
    void walk() {
        System.out.println("mixin.Dog is walking...");
    }

    /*
    *   개는 날 수 없지만 mixin.Animal 클래스를 상속받음으로 인해서
    *   클래스의 의미가 변질된다.
    * */
    @Override
    void fly() {
        System.out.println("mixin.Dog can fly??");
    }
}
