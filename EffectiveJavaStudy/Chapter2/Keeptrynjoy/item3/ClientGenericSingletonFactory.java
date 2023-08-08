package item3;

import java.util.function.UnaryOperator;

import static item3.GenericSingletonFactory.identityFunction;

public class ClientGenericSingletonFactory {
    public static void main(String[] args) {
        String[] strings = { "KIM", "LEE", "SEO" };
        UnaryOperator<String> sameString = identityFunction();
        for (String s : strings)
            System.out.println(sameString.apply(s));

        Number[] numbers = { 1, 2.0, 3L };
        UnaryOperator<Number> sameNumber = identityFunction();
        for (Number n : numbers) {
            System.out.println(sameNumber.apply(n));
        }

        System.out.println("sameString.equals(sameNumber) : "+ sameString.equals(sameNumber));
    }
}
