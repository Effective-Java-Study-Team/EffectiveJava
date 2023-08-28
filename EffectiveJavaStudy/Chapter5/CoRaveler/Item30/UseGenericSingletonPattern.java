package CoRaveler.Item30;

import java.util.function.UnaryOperator;

public class UseGenericSingletonPattern {
    public static void main(String[] args) {
        String[] strings = {"a", "b", "c"};
        Integer[] integers = {1, 2, 3};

        System.out.println();
        UnaryOperator<String> sameString = GenericSingletonPattern.identityFunction();
        System.out.println("sameString = " + sameString);
        for(String s : strings)
            System.out.println(sameString.apply(s));



        System.out.println();
        UnaryOperator<Integer> sameInteger = GenericSingletonPattern.identityFunction();
        System.out.println("sameInteger = " + sameInteger);
        for(Integer i : integers)
            System.out.println(sameInteger.apply(i));
        System.out.println();

        String sameStringAddr = sameString.toString();
        String sameIntegerAddr = sameInteger.toString();

        System.out.println("(sameString == sameInteger) = " + sameIntegerAddr.equals(sameStringAddr));
    }
}
