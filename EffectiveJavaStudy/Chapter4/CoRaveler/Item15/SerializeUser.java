package CoRaveler.Item15;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SerializeUser {
    public static void main(String[] args) {
        UserSerializable user = new UserSerializable("user", "1234");
        UserSerializableTransientUsed transientUser = new UserSerializableTransientUsed("userTransient", "1234");

        try{
            // user serialize
            FileOutputStream fileOutputStream1 = new FileOutputStream("user.ser");
            ObjectOutputStream objectOutputStream1 = new ObjectOutputStream(fileOutputStream1);
            objectOutputStream1.writeObject(user);

            objectOutputStream1.close();
            fileOutputStream1.close();

            // transientUser serializer
            FileOutputStream fileOutputStream2 = new FileOutputStream("transientUser.ser");
            ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(fileOutputStream2);
            objectOutputStream2.writeObject(transientUser);

            objectOutputStream2.close();
            fileOutputStream2.close();
        } catch(IOException e ) {
            e.printStackTrace();
        }
    }
}
