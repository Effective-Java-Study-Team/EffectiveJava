import java.util.Objects;

public class Card {

    private String cardNum;

    public Card(String cardNum) {
        this.cardNum = cardNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card card)) return false;
        return cardNum.equals(card.cardNum);
    }

    @Override
    public int hashCode() {
        int result = 31 * cardNum.hashCode();
        return result;
    }
}
