import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializableTest {

    public static void main(String[] args) {
        D d = new D(30, 40, 50);
        byte[] serializedD = serialize(d);

        D deserializedD = (D) deserialize(serializedD);

        System.out.println(d);
        System.out.println(deserializedD);
    }

    public static byte[] serialize(Object instance) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try (bos; ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(instance);
        } catch (Exception e) {

        }

        return bos.toByteArray();
    }

    public static Object deserialize(byte[] serializedData) {
        ByteArrayInputStream bis = new ByteArrayInputStream(serializedData);
        try (bis; ObjectInputStream ois = new ObjectInputStream(bis)) {
            return ois.readObject();
        } catch (Exception e) {

        }

        return null;
    }
}
