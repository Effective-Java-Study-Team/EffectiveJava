public class Tv implements Comparable<Tv> {

    private String name;
    private int price;

    public Tv(String name, int price) {
        this.name = name;
        this.price = price;
    }


    @Override
    public int compareTo(Tv o) {
        int result = name.compareTo(o.name);

        if (result == 0) result = Integer.compare(price, o.price);

        return result;
    }
}
