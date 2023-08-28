package CoRaveler.Item28;

public class UseVarargsAndGenerics {
    public static void main(String[] args) {
        VarargsAndGenerics.printArrayContent("apple", "banana", "cherry");
        VarargsAndGenerics.printArrayContent("apple", 1, 2);
        VarargsAndGenerics.printArrayContent(1, 2, 1555L);
    }
}