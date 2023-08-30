package CoRaveler.Item33;

public class UseFavorites {
    public static void main(String[] args) {
        Favorites favorites = new Favorites();  // 타입 안전 이종 컨테이너

        favorites.putFavorite(Integer.class, 42);
        favorites.putFavorite(String.class, "Kotlin");
        favorites.putFavorite(Class.class, Favorites.class);

        Integer favorite = favorites.getFavorite(Integer.class);
        String favorite1 = favorites.getFavorite(String.class);
        Class<?> favorite2 = favorites.getFavorite(Class.class);

        System.out.println(favorite);
        System.out.println(favorite1);
        System.out.println(favorite2);
        System.out.println();

        favorites.putFavorite((Class)Integer.class, "abc");
        Class favoriteInt = favorites.getFavorite(Class.class);
        System.out.println("favoriteInt = " + favoriteInt);
    }
}
