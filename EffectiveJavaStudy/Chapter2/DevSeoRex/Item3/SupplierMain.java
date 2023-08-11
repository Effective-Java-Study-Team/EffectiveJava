package ch2.Item3;;

public class SupplierMain {

    public static void main(String[] args) {

        // 인자 없이 값만 반환하는 getInstance 메서드를 Supplier에 준하게 사용할 수 있다.
        ElvisWithSupplier.leaveTheBuilding(ElvisWithSupplier::getInstance);
    }
}
