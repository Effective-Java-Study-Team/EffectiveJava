package CoRaveler.Item26;

import org.w3c.dom.Node;

import java.util.HashSet;
import java.util.Set;

public class CapturedType {
    public static void main(String[] args) {
        Set<?> set = new HashSet<>();
//        set.add(1);

        Set<? extends Parent> set2 = new HashSet<>();
//        set2.add(1);
    }
    
    static class Parent {
        int x;
    }
    
    static class Child {
        int y;
    }
}
