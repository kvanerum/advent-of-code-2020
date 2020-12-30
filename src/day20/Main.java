package day20;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        List<Tile> tiles = readInput();

        // match tiles
        tiles.forEach(tile1 -> {
            tile1.findNeighbors(tiles);
        });
    }


    private static List<Tile> readInput() throws IOException {
        List<String> lines = Files.lines(Paths.get("src/day20/example.txt")).collect(Collectors.toList());

        List<Tile> result = new ArrayList<>();
        int fromIndex = 0;

        for (int i = 1; i < lines.size(); ++i) {
            if (lines.get(i).isEmpty()) {
                result.add(new Tile(lines.subList(fromIndex, i)));
                fromIndex = i + 1;
            }
        }

        return result;
    }
}
