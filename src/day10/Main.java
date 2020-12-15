package day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Integer> input = readInput();
        Collections.sort(input);

        // add 0 and max + 3
        input.add(0, 0);
        input.add(input.get(input.size() - 1) + 3);

        part1(input);

        long possibilities = calculatePossibilities(input, 0, new HashMap<>());
        System.out.println(possibilities);
    }

    private static void part1(List<Integer> input) {
        int difference1 = 0;
        int difference3 = 0;

        for (int i = 0; i < input.size() - 1; ++i) {
            int diff = input.get(i + 1) - input.get(i);
            if (diff == 1) {
                difference1 += 1;
            } else if (diff == 3) {
                difference3 += 1;
            } else {
                System.out.println("unknown difference: " + diff);
            }
        }

        System.out.println(difference1 * difference3);
    }

    private static long calculatePossibilities(List<Integer> input, int needle, Map<Integer, Long> resultMap) {
        if (input.size() - 1 == needle) {
            return 1;
        }

        int current = input.get(needle);
        needle++;
        long sum = 0;

        while (needle < input.size() && input.get(needle) - current <= 3) {
            if (resultMap.containsKey(needle)) {
                sum += resultMap.get(needle);
            } else {
                sum += calculatePossibilities(input, needle, resultMap);
            }
            needle++;
        }

        resultMap.putIfAbsent(needle, sum);
        return sum;
    }

    private static List<Integer> readInput() throws IOException {
        List<String> lines = Files.lines(Paths.get("src/day10/input.txt")).collect(Collectors.toList());

        return lines.stream().map(Integer::parseInt).collect(Collectors.toList());
    }
}
