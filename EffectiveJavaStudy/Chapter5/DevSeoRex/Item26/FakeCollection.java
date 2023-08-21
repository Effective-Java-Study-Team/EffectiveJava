import java.util.ArrayList;
import java.util.List;

public class FakeCollection<E> {

    private List<E> list = new ArrayList<>();

    public void add(E e) {
        list.add(e);
    }

    public boolean contains(E e) {
        return list.contains(e);
    }
}


