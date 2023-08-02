public class Latte extends Coffee {

    boolean isSweet;

    public Latte(int price, String beanName, boolean isSweet) {
        super(price, beanName);
        this.isSweet = isSweet;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Latte) || !super.equals(o)) return false;

        return isSweet == ((Latte) o).isSweet;
    }

}

