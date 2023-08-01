package CoRaveler.Item1;

import java.util.regex.Pattern;

public class PatternCheck {
    public static void main(String[] args) {
        String  regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$\n";

        Pattern p1 = Pattern.compile(regex);
        Pattern p2 = Pattern.compile(regex);

        System.out.println("(p1 == p2) = " + (p1 == p2));
        System.out.println("(p1.equals(p2)) = " + (p1.equals(p2)));
    }
}
