public class Person {

    private int age;
    private String name;

    public Person(int age, String name) {
        this.age = age;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (!(o instanceof Person)) {
            return false;
        }

        Person comparedPerson = (Person) o;

        return name.equals(comparedPerson.name) && age == comparedPerson.age;
    }
}



