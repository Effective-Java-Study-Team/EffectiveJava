class Sword implements Weapon {

    private int damage;
    private String name;

    public Sword(int damage, String name) {
        this.damage = damage;
        this.name = name;
    }

    public void attack() {
        System.out.println("물리 공격으로 " + damage + "만큼의 피해를 주었습니다.");
    }
}
