public class FrenchDog implements Dog {
    @Override
    public void walk() {

    }

    @Override
    public void bark() {

    }


    @Override
    public int compareTo(Animal o) {
        return o.compareTo(this);
    }
}
