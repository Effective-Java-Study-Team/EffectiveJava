package ch2.item7;

public class Caller {
    public static void main(String[] args) {
        Callee callee = new Callee();
        callee.setCallback(arg ->
            System.out.println("입력받은 메세지 > "+arg.getMsg())
        );

        callee.onInputMessage();


    }
}
