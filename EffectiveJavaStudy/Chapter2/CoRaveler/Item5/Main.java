package CH2.Item5;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        Supplier<Tile> t = Tile::new;

        // 일반 Tile 로 이루어진 Mosaic 을 생성합니다.
        Mosaic mosaic1 = new Mosaic(Tile::new, 5);
        mosaic1.draw();

        // 색이 있는 Tile 로 이루어진 Mosaic 을 생성합니다.
        Mosaic mosaic2 = new Mosaic(() -> new ColorTile("red"), 5);
        mosaic2.draw();

    }
}

class Tile {
    public void draw() {
        System.out.println("Drawing a Tile");
    }
}

class ColorTile extends Tile {
    private final String color;

    public ColorTile(String color) {
        this.color = color;
    }

    @Override
    public void draw() {
        System.out.println("Drawing a " + color + " tile");
    }
}

class Mosaic {
    private final List<Tile> tiles;

    // 생성자에서 Tile 의 Supplier 를 받아서 Mosaic 을 만듭니다.
    public Mosaic(Supplier<? extends Tile> tileFacotry, int numTiles) {
        tiles = new ArrayList<>();
        for (int i = 0; i < numTiles; i++) {
            tiles.add(tileFacotry.get());
        }
    }

    public void draw() {
        for(Tile tile : tiles) {
            tile.draw();
        }
    }
}