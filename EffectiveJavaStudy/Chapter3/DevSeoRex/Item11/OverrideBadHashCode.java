public class OverrideBadHashCode {

    public static void main(String[] args) {
        BadHashCode badHashCode1 = new BadHashCode(100, 100);
        BadHashCode badHashCode2 = new BadHashCode(100, 100);
        BadHashCode badHashCode3 = new BadHashCode(100, 50);

        System.out.println("badHashCode1 = " + badHashCode1.hashCode());
        System.out.println("badHashCode2 = " + badHashCode2.hashCode());
        // 논리적 동치가 아닌데 매번 같은 hashCode 반환 -> O(1) 시간 복잡도 내로 탐색 불가 O(n)으로 복잡도 증가
        System.out.println("badHashCode3 = " + badHashCode3.hashCode());
    }
}
