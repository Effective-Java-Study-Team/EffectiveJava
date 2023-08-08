class InnerPrivateClass {

    private int x;
    private int y;

    public int sum() {
        return x + y + Inner.j + Inner.k;
    }


    private static class Inner {
        static int j;
        static int k;
    }
}
