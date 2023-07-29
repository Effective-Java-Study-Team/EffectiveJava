package com.adoptpet.server.adopt.dto.test;

public class FrenchDictionary implements Lexicon {

    private static final FrenchDictionary INSTANCE = new FrenchDictionary();

    private FrenchDictionary() {}

    public static FrenchDictionary getFrenchDictionary() {
        return INSTANCE;
    }

    @Override
    public void findWord(String word) {
        System.out.println("find word with french .." + word);
    }
}
