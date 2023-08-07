class Magician extends Adventurer {


    private Weapon weapon;

    public void attack() {
        weapon.attack();
    }

    public void magicClaw() {
        SkillOfMagician.magicClaw();
    }


    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    // 한 클래스에서만 사용하는 클래스라면 private static inner class로 사용하자
    private static class SkillOfMagician {

        private static void magicClaw() {
            System.out.println("magicClaw!!!");
        }
    }
}
