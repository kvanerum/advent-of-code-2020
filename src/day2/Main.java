package day2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        List<String> input = readInput();

        long count1 = input.stream().map(Password::new).filter(Password::isValidPart1).count();
        System.out.println(count1);

        long count2 = input.stream().map(Password::new).filter(Password::isValidPart2).count();
        System.out.println(count2);
    }

    private static List<String> readInput() throws IOException {
        return Files.lines(Paths.get("src/day2/input.txt")).collect(Collectors.toList());
    }
}
