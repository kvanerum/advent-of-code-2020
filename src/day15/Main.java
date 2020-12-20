package day15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Integer> game = readInput();
        Map<Integer, List<Integer>> occurrencesMap = new HashMap<>();

        for (int i = 0; i < game.size(); ++i) {
            occurrencesMap.putIfAbsent(game.get(i), new ArrayList<>());
            occurrencesMap.get(game.get(i)).add(i);
        }

        for (int turn = game.size(); turn < 30000000; ++turn) {
            int previous = game.get(turn - 1);

            // find previous occurrence
            List<Integer> previousOccurrences = occurrencesMap.getOrDefault(previous, Collections.emptyList());
            if (previousOccurrences.size() < 2) {
                game.add(0);
            } else {
                game.add(previousOccurrences.get(previousOccurrences.size() - 1) -
                         previousOccurrences.get(previousOccurrences.size() - 2));
            }

            occurrencesMap.putIfAbsent(game.get(turn), new ArrayList<>());
            occurrencesMap.get(game.get(turn)).add(turn);
        }

        System.out.println(game.get(game.size() - 1));
    }


    private static List<Integer> readInput() throws IOException {
        List<String> lines = Files.lines(Paths.get("src/day15/input.txt")).collect(Collectors.toList());

        return Arrays.stream(lines.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());
    }
}
