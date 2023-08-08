package CoRaveler.Item15;

public class UseThreadNotSafeArrayList {
    public static void main(String[] args) {
        ThreadNotSafeArrayLIst l = new ThreadNotSafeArrayLIst();

        // 10개의 스레드를 생성하여 동시에 list에 접근하게 합니다.
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 1; j <= 6; j++) {
                    l.list.add("Test" + j);
                }
            }).start();
        }

        // 스레드들이 모두 작업을 완료할 때까지 대기합니다.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // list의 크기를 출력합니다.
        System.out.println("Size of list: " + l.list.size());
    }
}
