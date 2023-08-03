public class Wallet {

    private int money;
    private String owner;
    private Card card;

    public Wallet(int money, String owner, String cardNum) {
        this.money = money;
        this.owner = owner;
        this.card = new Card(cardNum);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (!(obj instanceof Wallet wallet)) return false;

        return card.equals(wallet.card) && wallet.money == money
                && wallet.owner.equals(owner);
    }

    @Override
    public int hashCode() {
        // 1. result 값을 객체의 핵심 필드 중 첫 번째 필드를 Type.hashCode(f) 로 초기화한다.
        int result = Integer.hashCode(money);

        // 2. 그 다음 필드부터 result = 31 * result + Type.hashCode(f) 로 재대입 한다.
        result = 31 * result + owner.hashCode();

        /*
        *   핵심 필드인 참조형 필드의 hashCode가 적절히 오버라이딩 되어 있지 않다면, 모두 다른 hashCode를 반환하게 된다.
        *   적절히 오버라이딩이 되어 있는데 호출하지 않을 경우에는 참조형 필드 이외의 값이 모두 같다면,
        *   참조형 필드의 상태와 상관없이, 모두 같은 hashCode를 반환하게 되는 문제가 생긴다.
        * */
        result = 31 * result + card.hashCode();

        return result;
    }


}
