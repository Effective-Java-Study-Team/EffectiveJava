import java.util.ArrayList;
import java.util.List;

public class NonSymmetryMain {

    public static void main(String[] args) {
        CaseInsensitiveString caseInsensitiveString = new CaseInsensitiveString("Apple");
        String word = "Apple";

        System.out.println(caseInsensitiveString.equals(word)); // true
        System.out.println(word.equals(caseInsensitiveString)); // false


        List<CaseInsensitiveString> list = new ArrayList<>();
        list.add(caseInsensitiveString);

        System.out.println(list.contains(word));


    }
}


