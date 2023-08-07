class SwordMaster extends Adventurer {

    private Weapon weapon;


    public void attack() {
        weapon.attack();
    }

    public void dragonRoar() {
        SkillOfSwordMaster.dragonRoar();
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }


    private static class SkillOfSwordMaster {

        private static void dragonRoar() {
            System.out.println("dragonRoar!!!");
        }
    }
}
