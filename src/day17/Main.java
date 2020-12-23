package day17;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        List<List<Character>> input = readInput();

        HyperCube hyperCube = new HyperCube(3, input);

        for (int turn = 0; turn < 6; ++turn) {
            hyperCube.doCycle();
        }

        System.out.println(hyperCube.activeCount());

        hyperCube = new HyperCube(4, input);

        for (int turn = 0; turn < 6; ++turn) {
            hyperCube.doCycle();
        }

        System.out.println(hyperCube.activeCount());
    }

    private static List<List<Character>> readInput() throws IOException {
        List<String> lines = Files.lines(Paths.get("src/day17/input.txt")).collect(Collectors.toList());

        return lines.stream().map(line -> line.chars().mapToObj(c -> (char) c).collect(Collectors.toList())).collect(Collectors.toList());
    }
}
