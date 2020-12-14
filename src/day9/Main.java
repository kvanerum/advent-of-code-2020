package day9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Long> input = readInput();

        long error = findError(input, 25);
        System.out.println(error);

        long weakness = findWeakness(error, input);
        System.out.println(weakness);
    }

    private static Long findError(List<Long> input, int preamble) {
        for (int i = preamble; i < input.size(); ++i) {
            if (!isValidNumber(input.get(i), input.subList(i - preamble, i))) {
                return input.get(i);
            }
        }

        return null;
    }

    private static long findWeakness(long error, List<Long> input) {
        for (int setSize = 2; setSize < input.size(); ++setSize) {
            for (int i = 0; i <= input.size() - setSize; ++i) {
                long sum = 0;
                for (int j = i; j < i + setSize; j++) {
                    sum += input.get(j);
                }

                if (sum == error) {
                    return sumSmallestLargest(input, i, i + setSize);
                }
            }
        }

        return 0L;
    }

    private static long sumSmallestLargest(List<Long> input, int from, int until) {
        long smallest = input.get(from);
        long largest = input.get(from);

        for (int i = from; i < until; ++i) {
            long n = input.get(i);
            if (n > largest) {
                largest = n;
            } else if (n < smallest) {
                smallest = n;
            }
        }

        return smallest + largest;
    }

    private static boolean isValidNumber(long number, List<Long> previous) {
        return previous.stream().anyMatch(n -> previous.contains(number - n) && n != number / 2);
    }


    private static List<Long> readInput() throws IOException {
        List<String> lines = Files.lines(Paths.get("src/day9/input.txt")).collect(Collectors.toList());

        return lines.stream().map(Long::parseLong).collect(Collectors.toList());
    }
}
