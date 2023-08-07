public class AdventureMain {


    public static void main(String[] args) {
        Magician magician = new Magician();
        magician.setWeapon(new Sword(1000, "소드의 정석"));
        magician.attack();

        SwordMaster master = new SwordMaster();
        master.setWeapon(new MagicWand(2000, "완드의 정석"));
        master.attack();
    }
}
