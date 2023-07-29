package com.adoptpet.server.adopt.dto.test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SpellChecker {

    private final Lexicon dictionary;

    public SpellChecker(Supplier<? extends Lexicon> lexiconFactory) {
        this.dictionary = lexiconFactory.get();
    }

    public void findWord(String word) {
        dictionary.findWord(word);
    }

    public static boolean isValid(String word) {
        boolean isValid = word.isBlank();
        return isValid;
    }

    public static List<String> suggestions(String typo) {
        return new ArrayList<>();
    }
}

