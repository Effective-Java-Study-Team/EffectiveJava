package com.adoptpet.server.adopt.dto.test;

public class LexiconMain {

    public static void main(String[] args) {
        SpellChecker englishChecker = new SpellChecker(EnglishDictionary::getEnglishDictionary);
        SpellChecker koreanChecker = new SpellChecker(KoreaDictionary::getKoreaDictionary);
        SpellChecker frenchChecker = new SpellChecker(FrenchDictionary::getFrenchDictionary);

        // 영어 사전으로 단어 검색
        englishChecker.findWord("Hello");
        koreanChecker.findWord("안녕");
        frenchChecker.findWord("Bonjour!");


    }
}


