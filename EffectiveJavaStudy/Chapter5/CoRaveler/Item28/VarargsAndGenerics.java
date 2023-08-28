package CoRaveler.Item28;

public class VarargsAndGenerics {
    public static <T> void printArrayContent(T... elements) {
        System.out.println("elements.getClass() = " + elements.getClass());
        for (T element : elements) {
            System.out.println(element + " ");
        }
        System.out.println();
    }
}
