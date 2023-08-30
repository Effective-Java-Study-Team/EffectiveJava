import java.util.List;

public class SuperTypeTokenTest {

    public static void main(String[] args) {

        Favorites favorites = new Favorites();
        favorites.putFavorite(List.class, List.of(1, 2));
        favorites.putFavorite(List.class, List.of("a", "b"));

        System.out.println(favorites.getFavorite(List.class));

        RefactorFavorite map = new RefactorFavorite();
        TypeReference<List<String>> list = new TypeReference<>() {};
        map.putFavorite(list, List.of(1, 2, 3));

        System.out.println(map.get(list));
    }
}
