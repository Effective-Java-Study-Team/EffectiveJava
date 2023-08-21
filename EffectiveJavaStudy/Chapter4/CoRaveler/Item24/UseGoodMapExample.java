package CoRaveler.Item24;

public class UseGoodMapExample {
    public static void main(String[] args) {
        GoodMapExample gme = new GoodMapExample();
        for(int i = 1; i <= 5; i++) {
            gme.putEntry(i, i);
        }
        gme.printAllElements(); // 애초에 this 참조가 안되지만,
                                // 각각의 Entry 는 this 를 사용할 필요도 없으니
                                // 상관이 없다!
    }
}
