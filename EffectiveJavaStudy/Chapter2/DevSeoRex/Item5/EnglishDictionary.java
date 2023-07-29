package com.adoptpet.server.adopt.dto.test;

public class EnglishDictionary implements Lexicon {

    private static final EnglishDictionary INSTANCE = new EnglishDictionary();

    private EnglishDictionary() {}

    public static EnglishDictionary getEnglishDictionary() {
        return INSTANCE;
    }

    @Override
    public void findWord(String word) {
        System.out.println("find word with english .." + word);
    }
}


