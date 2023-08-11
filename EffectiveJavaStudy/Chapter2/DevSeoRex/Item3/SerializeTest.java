package ch2.Item3;

public class SerializeTest {

    public static void main(String[] args) {
        MyInt instance = MyInt.getInstance();
        byte[] serializedData = SerializationUtils.serialize(instance);
        MyInt result = (MyInt) SerializationUtils.deserialize(serializedData);

        System.out.println(result == instance);
        System.out.println(result.equals(instance));

        System.out.println("instance = " + instance);
        System.out.println("result = " + result);
    }
}

