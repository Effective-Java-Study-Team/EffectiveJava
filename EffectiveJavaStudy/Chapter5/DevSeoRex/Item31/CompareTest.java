import java.util.List;

public class CompareTest {


    public static void main(String[] args) {
        List<Dog> dogs = List.of(new FrenchDog(), new FrenchDog(), new FrenchDog());
        // java: method max in class CompareUtils cannot be applied to given types;
        //  required: java.util.List<E>
        //  found:    java.util.List<Dog>
        //  reason: inference variable E has incompatible equality constraints Animal,Dog
//        CompareUtils.max(dogs);
        CompareUtils.maxRefactor(dogs);
    }
}
