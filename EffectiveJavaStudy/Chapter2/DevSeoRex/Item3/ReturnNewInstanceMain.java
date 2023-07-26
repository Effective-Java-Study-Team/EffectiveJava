package ch2.item3;

public class ReturnNewInstanceMain {

    public static void main(String[] args) {


        Elvis elvis1 = Elvis.getInstance();
        Elvis elvis2 = Elvis.getInstance();

        ElvisReturnNewInstance newInstance1 = ElvisReturnNewInstance.getInstance();
        ElvisReturnNewInstance newInstance2 = ElvisReturnNewInstance.getInstance();

        System.out.println(elvis1.equals(elvis2));
        System.out.println(newInstance1.equals(newInstance2));
    }
}
