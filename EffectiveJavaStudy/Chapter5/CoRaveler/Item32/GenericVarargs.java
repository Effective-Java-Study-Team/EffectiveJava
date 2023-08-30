package CoRaveler.Item32;

public class GenericVarargs {
    public static <T> void printItems(T... items) {
        System.out.println("items.getClass() = " + items.getClass());
        for(T item : items) {
            System.out.println(item.getClass());
            System.out.println(item);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println();
        printItems(1, 2, 3);
        printItems('a','b','c');
    }
}
