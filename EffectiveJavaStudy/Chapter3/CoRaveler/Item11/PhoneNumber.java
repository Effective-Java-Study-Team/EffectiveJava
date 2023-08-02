package CoRaveler.Item11;

import java.util.Objects;

public class PhoneNumber {
    int first, second, third;
    String name;

    public PhoneNumber(int first, int second, int third, String name) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneNumber that)) return false;
        return first == that.first && second == that.second && third == that.third && Objects.equals(name, that.name);
    }


    // TODO : 책에서 나온 hashCode() 구현, 나쁘지 않은 방법
    @Override
    public int hashCode() {
        int result = Integer.hashCode(first);
        result = 31 * result + Integer.hashCode(second);
        result = 31 * result + Integer.hashCode(third);
        result = 31 * result + name.hashCode();
        return result;
    }

    // TODO : 단순 int return hashCode() 사용한 경우
    public int hashCode2() {
        return 42;
    }

    // TODO : Objects.hash(Objects... values) 를 이용한 한 줄 짜리 hashCode
    public int hashCode3() {
        return Objects.hash(first, second, third, name);
    }


    public static void main(String[] args) {
        PhoneNumber num1 = new PhoneNumber(111, 1234, 5678, "코래블러");
        PhoneNumber num2 = new PhoneNumber(111, 1234, 5678, "코래블러");
        PhoneNumber num3 = new PhoneNumber(111, 112, 112, "DevSeoRex");

        // 실제 값 비교
        System.out.println("31을 곱하는 경우 = " + num1.hashCode());
        System.out.println();
        System.out.println("단순한 int return = " + num2.hashCode());
        System.out.println();
        System.out.println("Objects.hash() = " + num3.hashCode());
        System.out.println();
        System.out.println("(num1.hashCode() == num2.hashCode()) = " + (num1.hashCode() == num2.hashCode()));

        // PhoneNumber 의 hashCode 와 Objects.hash 의 성능 비교
        // test the first hashCode method
        long start = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            num1.hashCode();
        }
        long end = System.nanoTime();
        System.out.println("Time taken by 31을 곱하는 경우: " + (end - start) + " ns");

        // test the second hashCode method (after uncommenting the method in the class)
        start = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            num2.hashCode2();
        }
        end = System.nanoTime();
        System.out.println("Time taken by 단순한 int return: " + (end - start) + " ns");

        // test the third hashCode method (after uncommenting the method in the class)
        start = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            num3.hashCode3();
        }
        end = System.nanoTime();
        System.out.println("Time taken by Objects.hash(): " + (end - start) + " ns");
    }
}
