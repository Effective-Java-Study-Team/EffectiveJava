package com.adoptpet.server.adopt.dto.test;

import java.util.ArrayList;
import java.util.List;

public class SpellCheckerWithStaticUtility {
//    public static final Lexicon dictionary = new Lexicon();

    // 인스턴스 생성 방지
    private SpellCheckerWithStaticUtility() {}

    public static boolean isValid(String word) {
        boolean isValid = word.isBlank();
        return isValid;
    }

    public static List<String> suggestions(String typo) {
        return new ArrayList<>();
    }
}


