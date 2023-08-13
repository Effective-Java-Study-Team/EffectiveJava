package ch2.item7;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class CacheExemple {

    static class Animal{

        private final String species;

        public Animal(String species) {
            this.species = species;
        }
    }


    public static void main(String[] args) {

//        Map<Animal, String> map = new HashMap<>();
        WeakHashMap<Animal, String> map = new WeakHashMap<>();

        Animal tiger = new Animal("tiger");
        Animal monkey = new Animal("monkey");

        map.put(tiger,"호랑이");
        map.put(monkey,"원숭이");

        tiger = null;

        System.gc();

        map.entrySet().forEach(
                System.out::println
        );
    }

}
