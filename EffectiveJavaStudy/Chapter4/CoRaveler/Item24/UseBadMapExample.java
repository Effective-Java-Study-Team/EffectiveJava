package CoRaveler.Item24;

public class UseBadMapExample {
    public static void main(String[] args) {
        BadMapExample bme = new BadMapExample();
        System.out.println("bme = " + bme);
        System.out.println();
        for(int i = 1; i <= 5; i++) {
            bme.putEntry(i, i);
        }
        bme.printAllElements(); // 각각의 entry 가 쓸데없이
                                // bme 에 대한 참조를 가지고 있다.
    }
}
