package CH2.Item2;

public class Main {
    public static void main(String[] args) {
        NutritionFactsBuilder nfb = new NutritionFactsBuilder
                .Builder(240, 8)
                .calories(100)
                .sodium(35)
                .carbohydrate(27)
                .build();

    }
}