import java.util.List;

public class Market implements Cloneable {

    private String name;
    private List<Item> saleItems;

    public Market(String name, List<Item> saleItems) {
        this.name = name;
        this.saleItems = saleItems;
    }

    @Override
    protected Market clone() throws CloneNotSupportedException {
        return (Market) super.clone();
    }
}

class Item {

    private String id;
    private String itemName;
    private int price;

    public Item(String id, String itemName, int price) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
    }
}
