package day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Integer> input = readInput();
        Collections.sort(input);

        int difference1 = 0;
        int difference3 = 0;
    }

    private static List<Integer> readInput() throws IOException {
        List<String> lines = Files.lines(Paths.get("src/day10/example.txt")).collect(Collectors.toList());

        return lines.stream().map(Integer::parseInt).collect(Collectors.toList());
    }
}
