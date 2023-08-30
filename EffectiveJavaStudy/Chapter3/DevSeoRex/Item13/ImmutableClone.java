package DevSeoRex.Item13;

public class ImmutableClone {

    public static void main(String[] args) throws CloneNotSupportedException {
        Word word1 = new Word("Apple");
        Word word2 = word1.clone();

        word2.setWord("Grape");

        System.out.println("word1 = " + word1);
        System.out.println("word2 = " + word2);
    }
}
