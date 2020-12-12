package day1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Integer> input = readInput();

        // generate all permutations of size
        List<List<Integer>> permutations = generatePermutations(input, Collections.emptyList(), 3);

        // find the one that sums to 2020
        Optional<List<Integer>> numbers = permutations.stream().filter(
                permutation -> permutation.stream().reduce(0, Integer::sum) == 2020).findFirst();

        // calculate the result
        numbers.ifPresent(integers -> System.out.println(integers.stream().reduce(1, (a, b) -> a * b)));
    }

    private static List<List<Integer>> generatePermutations(List<Integer> remainingElements, List<Integer> currentSet, int size) {
        if (currentSet.size() == size) {
            return Collections.singletonList(currentSet);
        } else {
            return remainingElements.stream().map(remainingElement -> {
                List<Integer> newSet = new ArrayList<>(currentSet);
                newSet.add(remainingElement);
                List<Integer> newRemaining = new ArrayList<>(remainingElements);
                newRemaining.remove(remainingElement);

                return generatePermutations(newRemaining, newSet, size);
            }).collect(ArrayList::new, List::addAll, List::addAll);
        }
    }

    private static List<Integer> readInput() throws IOException {
        return Files.lines(Paths.get("src/day1/input.txt")).map(Integer::parseInt).collect(Collectors.toList());
    }
}
