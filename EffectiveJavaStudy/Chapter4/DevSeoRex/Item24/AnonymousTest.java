package DevSeoRex.Item24;

public class AnonymousTest {

    int k = 10;

    void run() {
        new Dog() {
            @Override
            void balk() {
                System.out.println("k = " + k);
            }

        }.balk();
    }

    public static void main(String[] args) {

        Dog dog = new Dog() {
            int a = 10;
            int b = 20;
            @Override
            void balk() {
                System.out.println("age = " + age);
                System.out.println("money = " + money);
                System.out.println("a + b = " + (a + b));
//                System.out.println("k = " + k);  // 바깥 클래스 참조 불가
            }
        };

        dog.balk();
        System.out.println(dog.age);
        System.out.println(dog.money);

        System.out.println(dog.getClass().getName());
    }
}

