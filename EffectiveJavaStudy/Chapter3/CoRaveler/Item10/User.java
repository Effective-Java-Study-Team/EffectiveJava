package CoRaveler.Item10;

import java.util.Objects;

public class User {
    private String name;
    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;

        boolean internetStatus = checkInternetStatus();

        return age == user.age && Objects.equals(name, user.name) && internetStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    private boolean checkInternetStatus() {
        int num = (int) (Math.random() * 50);

        if (num < 25) return true;
        else return false;
    }
}
