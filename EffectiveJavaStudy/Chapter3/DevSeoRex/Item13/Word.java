package DevSeoRex.Item13;

public class Word implements Cloneable {

    private String word;

    public Word(String word) {
        this.word = word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                '}';
    }

    @Override
    protected Word clone() throws CloneNotSupportedException {
        /*
        *   불변객체나 기본 타입을 참조하는 경우
        *   super.clone(Object)을 호출하는 것으로 족하다.
        * */

        return (Word) super.clone();
    }
}
