public class PrivateInstanceEquals {

    public static void main(String[] args) {

        PrivateInstance instance1 = PrivateInstance.getInstance();
        PrivateInstance instance2 = PrivateInstance.getInstance();

        /*
        *   Object 클래스의 기본 equals를 사용하기 때문에
        *   a.equals(b)와 a == b 는 같은 의미이다.
        * */

        System.out.println("instance1 == instance2 ? " + (instance1 == instance2)); // true
        System.out.println("instance1.eqauls(instance2) ? " + instance1.equals(instance2)); // true

    }
}




