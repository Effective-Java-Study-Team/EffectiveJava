package DevSeoRex.Item13;

public class Dog {

    private String name;
    private int age;

    public Dog(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // 복사 생성자
    public Dog(Dog dog) {
        this.name = dog.getName();
        this.age = dog.getAge();
    }

    // 복사 팩터리 메서드
    public static Dog newInstance(Dog dog) {
        return new Dog(dog.getName(), dog.getAge());
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }
}
