package CoRaveler.Item28;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GenericChooser<T> {
    private final T[] choiceArray;

    public GenericChooser(Collection<T> choices) {
        choiceArray = (T[]) choices.toArray();
    }

    public T choose() {
        Random rnd = ThreadLocalRandom.current();
        return choiceArray[rnd.nextInt(choiceArray.length)];
    }
}
