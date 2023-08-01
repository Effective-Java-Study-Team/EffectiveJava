public abstract class Coffee {

    private final int price;
    private final String beanName;

    protected Coffee(int price, String beanName) {
        this.price = price;
        this.beanName = beanName;
    }




    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Coffee)) return false;

        Coffee coffee = (Coffee) o;

        return price == coffee.price && beanName.equals(coffee.beanName);
    }

}

