public class Americano extends Coffee {

    boolean isHot;

    public Americano(int price, String beanName, boolean isHot) {
        super(price, beanName);
        this.isHot = isHot;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Americano) || !super.equals(o)) return false;

        return isHot == ((Americano) o).isHot;
    }
}
