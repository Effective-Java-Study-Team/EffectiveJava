package CoRaveler.Item34;

public class WeightTable {
    public static void main(String[] args) {
        double earthW = 185;
        double mass = earthW / Planet.EARTH.getSurfaceGravity();

        for(Planet p : Planet.values()) {
            System.out.printf("%s 에서의 무게는 %f이다.\n",p, p.getSurfaceWeight(mass));
        }
    }
}
