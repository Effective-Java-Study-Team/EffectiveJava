import java.util.HashMap;

public class ReferencedFieldHashCode {

    public static void main(String[] args) {

        Wallet wallet1 = new Wallet(1000, "Rex", "1111");
        Wallet wallet2 = new Wallet(1000, "Rex", "1112");
        Wallet wallet3 = new Wallet(1000, "Rex", "1111");

        System.out.println("wallet1 = " + wallet1.hashCode());
        System.out.println("wallet2 = " + wallet2.hashCode());
        System.out.println("wallet3 = " + wallet3.hashCode());

        HashMap<Wallet, String> map = new HashMap<>();
        map.put(wallet1, "wallet1");
        map.put(wallet2 ,"wallet2");

        System.out.println(map.get(wallet3));
        System.out.println(map.get(wallet2));
        System.out.println(map.get(wallet1));
    }
}
