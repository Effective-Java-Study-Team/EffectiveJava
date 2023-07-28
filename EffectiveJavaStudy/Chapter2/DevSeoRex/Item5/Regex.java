package com.adoptpet.server.adopt.dto.test;

public class Regex {

    static boolean isValid(String sentence) {
        return sentence.matches("(\\W|^) stock\\s{0,3}tip(s){0,1}(\\W|$)");
    }
}
