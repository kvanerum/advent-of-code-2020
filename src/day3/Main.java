package day3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        List<List<Character>> input = readInput();

        System.out.println(getNumberOfTrees(input, 3, 1));

        System.out.println(
                getNumberOfTrees(input, 1, 1) * getNumberOfTrees(input, 3, 1) * getNumberOfTrees(input, 5,
                        1) * getNumberOfTrees(input, 7, 1) * getNumberOfTrees(input, 1, 2));
    }

    private static long getNumberOfTrees(List<List<Character>> map, int deltaX, int deltaY) {
        long encounters = 0;
        int row = 0;
        int column = 0;

        while (row < map.size()) {
            if (map.get(row).get(column % map.get(row).size()) == '#') {
                encounters++;
            }

            row += deltaY;
            column += deltaX;
        }

        return encounters;
    }

    private static List<List<Character>> readInput() throws IOException {
        return Files.lines(Paths.get("src/day3/input.txt")).map(
                line -> line.chars().mapToObj(c -> (char) c).collect(Collectors.toList())).collect(Collectors.toList());
    }
}
