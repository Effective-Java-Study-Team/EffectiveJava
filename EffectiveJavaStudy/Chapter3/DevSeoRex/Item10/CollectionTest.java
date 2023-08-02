import java.util.*;

public class CollectionTest {

    public static void main(String[] args) {
        Person person = new Person(20, "REX");

        Set<Person> set1 = new HashSet<>();
        Set<Person> set2 = new LinkedHashSet<>();

        List<Person> list1 = new LinkedList<>();
        List<Person> list2 = new ArrayList<>();

        list1.add(person);
        list2.add(person);

        set1.add(person);
        set2.add(person);


        System.out.println(set1.equals(set2));
        System.out.println(list1.equals(list2));
    }
}
