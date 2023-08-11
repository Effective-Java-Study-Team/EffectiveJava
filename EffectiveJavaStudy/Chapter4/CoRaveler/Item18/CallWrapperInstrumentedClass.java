package CoRaveler.Item18;

import java.util.Set;

public class CallWrapperInstrumentedClass {
    public static void main(String[] args) {
    }

    static void walk(Set<Person> people) {  // Person 객체에 대한 계측을 walk 할 때만 별도로 하는 경우!
        WrapperInstrumentedClass<Person> wrapper = new WrapperInstrumentedClass<>(people);
        // ...
    }

    static class Person {
    }
}
