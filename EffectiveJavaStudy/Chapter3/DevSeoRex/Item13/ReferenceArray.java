import java.util.Arrays;

public class ReferenceArray {

    public static void main(String[] args) {
        Point p1 = new Point(10, 20);
        Point p2 = new Point(20, 30);

        // 객체 배열에서는 clone을 이용한 깊은 복사를 해도 주소값은 새로 만들어지지 않아, 직접 코드를 작성해야 한다.
        Point[] arr1 = {p1, p2};
        Point[] arr2 = arr1.clone();

        System.out.println(Arrays.toString(arr1));
        System.out.println(Arrays.toString(arr2));

        p2.setX(30);
        System.out.println();

        System.out.println(Arrays.toString(arr1));
        System.out.println(Arrays.toString(arr2));
    }
}
