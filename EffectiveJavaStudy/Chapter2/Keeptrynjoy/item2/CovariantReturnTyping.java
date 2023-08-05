package com.example.ch2.item2;

/**
* 공변 반환 타이핑
 * 하위 클래스의 메서드가 상위 클래스의 메서드가 정의한 반환타입이 아닌,
 * 그 하위 타입을 반환하는 기능. JAVA 5부터 추가됨
**/
public class CovariantReturnTyping {
    class Animal {
        public Animal reproduce() {
            return new Animal();
        }
    }

    class Dog extends Animal {
        @Override
        public Dog reproduce() {
            return new Dog();
        }
    }

}
