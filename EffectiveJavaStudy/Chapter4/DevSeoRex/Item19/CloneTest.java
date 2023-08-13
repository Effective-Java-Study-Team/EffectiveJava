public class CloneTest {
    public static void main(String[] args) {


        B b1 = new B(30, 40, 50);
        B b2 = b1.clone();

        System.out.println("b1 = " + b1);  //  b1 = B{j=50, x=10, y=40}
        System.out.println("b2 = " + b2);  //  b2 = B{j=50, x=30, y=40}
    }
}
