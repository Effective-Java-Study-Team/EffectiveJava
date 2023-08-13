package ch2.item6;

import java.util.regex.Pattern;

public class EmailChecker {
    private static final Pattern EMAIL = Pattern
            .compile("^[a-zA-Z0-9]+@[0-9a-zA-Z]+\\\\.[a-z]+$");

    static boolean isEmail(String e){
        return EMAIL.matcher(e).matches();
    }
}
