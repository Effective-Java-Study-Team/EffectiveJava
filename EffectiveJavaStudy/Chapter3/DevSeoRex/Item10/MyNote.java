import java.util.ArrayList;
import java.util.List;

public class MyNote {

    private List<String> Note = new ArrayList<>(400);

    private List<String> writtenPage = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;

        if (!(o instanceof MyNote myBag)) return false;

        return myBag.writtenPage.equals(writtenPage);
    }

}




