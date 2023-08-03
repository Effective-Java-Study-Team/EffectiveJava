public class CloneOverrideMethod {

    public static void main(String[] args) throws CloneNotSupportedException {
        SubClass subClass1 = new SubClass(10);
        SubClass subClass2 = subClass1.clone();

        System.out.println(subClass1);
        System.out.println(subClass2);
    }
}
