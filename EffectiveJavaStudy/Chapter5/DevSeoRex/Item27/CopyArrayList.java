import java.util.Arrays;

public class CopyArrayList {

    public static void main(String[] args) {

        String[] arr = {"1", "2"};

        // arraycopy: type mismatch: can not copy java.lang.String[] into java.lang.Integer[]
        Integer[] copy = Arrays.copyOfRange(arr, 0, 2, Integer[].class);
        System.out.println(Arrays.toString(copy));
    }

}
