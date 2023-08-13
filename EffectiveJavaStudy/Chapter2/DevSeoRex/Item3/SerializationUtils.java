package ch2.Item3;;

import java.io.*;

public class SerializationUtils {

    private SerializationUtils() {}


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

