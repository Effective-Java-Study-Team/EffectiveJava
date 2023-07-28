package com.adoptpet.server.adopt.dto.test;

public class KoreaDictionary implements Lexicon {

    private static final KoreaDictionary INSTANCE = new KoreaDictionary();

    private KoreaDictionary() {}

    public static KoreaDictionary getKoreaDictionary() {
        return INSTANCE;
    }

    @Override
    public void findWord(String word) {
        System.out.println("find word with korean .." + word);
    }


}

