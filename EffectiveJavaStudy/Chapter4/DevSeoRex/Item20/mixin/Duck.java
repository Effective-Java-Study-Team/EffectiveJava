package mixin;

public class Duck extends Animal {

    @Override
    void makeSound(String sound) {
        System.out.println("mixin.Duck Sound = " + sound);
    }

    @Override
    void walk() {
        System.out.println("mixin.Duck is walking...");
    }

    @Override
    void fly() {
        System.out.println("mixin.Duck is Fly!!");
    }
}
