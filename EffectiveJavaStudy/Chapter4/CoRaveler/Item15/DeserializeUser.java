package CoRaveler.Item15;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DeserializeUser {
    public static void main(String[] args) {
        try {
            // user deserialize
            FileInputStream fileInputStream1 = new FileInputStream("user.ser");
            ObjectInputStream objectInputStream1 = new ObjectInputStream(fileInputStream1);
            UserSerializable user = (UserSerializable) objectInputStream1.readObject();

            System.out.println(user);

            objectInputStream1.close();
            fileInputStream1.close();

            System.out.println();
            // userTransient deserialize
            FileInputStream fileInputStream2 = new FileInputStream("transientUser.ser");
            ObjectInputStream objectInputStream2 = new ObjectInputStream(fileInputStream2);
            UserSerializableTransientUsed transientUser = (UserSerializableTransientUsed) objectInputStream2.readObject();

            System.out.println(transientUser);

            objectInputStream2.close();
            fileInputStream2.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
