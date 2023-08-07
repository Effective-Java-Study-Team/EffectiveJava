package CoRaveler.Item15;

import java.io.Serializable;

public class UserSerializableTransientUsed implements Serializable {
    private String username;
    private transient String password;

    public UserSerializableTransientUsed(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserSerializableTransientUsed{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
