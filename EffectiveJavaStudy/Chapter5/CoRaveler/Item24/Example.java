package CoRaveler.Item24;

import java.util.Optional;

public class Example {
    public static void main(String[] args) {
        Animal a = new Animal() {
            @Override
            void bark() {
                System.out.println("bark");
                talk();
            }

            void talk() {
                System.out.println("talk in a human language");
            }

            // JDK1.8 이면 아래 코드들은 다 터짐
            static int a = 5;
            static String str = "abc";
            static final int b = 10;
        };

    }
}

abstract class Animal {
    abstract void bark();
}
