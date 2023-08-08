package CoRaveler.Item15;

import java.io.Serializable;

public class UserSerializable implements Serializable {
    private String username;
    private String password;

    public UserSerializable(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserSerializable{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
