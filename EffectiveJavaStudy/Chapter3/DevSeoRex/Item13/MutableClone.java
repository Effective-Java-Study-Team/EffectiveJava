package DevSeoRex.Item13;

public class MutableClone {

    public static void main(String[] args) throws CloneNotSupportedException {

        Stack stack1 = new Stack();
        stack1.push("obj1");

        Stack stack2 = stack1.clone();
        stack2.push("obj2");

        // Stack 클래스의 element 필드가 원본과 같은 배열을 참조하기 때문에 어느 쪽을 수정해도 불변식을 해친다.
        System.out.println("stack1 = " + stack1);
        System.out.println("stack2 = " + stack2);
    }
}
