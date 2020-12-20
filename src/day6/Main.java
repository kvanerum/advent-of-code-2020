package day6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        List<String> groups = readInput();

        long count = groups.stream().map(group ->
                group.trim().replace("\n", "").chars().mapToObj(c -> (char) c).collect(Collectors.toSet()).size()
        ).reduce(0, Integer::sum);
        System.out.println(count);

        long count2 = groups.stream().map(group -> {
            Map<Character, Integer> buckets = new HashMap<>();

            List<String> lines = group.lines().filter(line -> !line.isEmpty()).map(String::trim).collect(Collectors.toList());
            lines.forEach(line -> {
                for (Character c : line.toCharArray()) {
                    buckets.put(c, buckets.getOrDefault(c, 0) + 1);
                }
            });

            return buckets.entrySet().stream().filter(entry -> entry.getValue() == lines.size()).count();
        }).reduce(0L, Long::sum);
        System.out.println(count2);
    }

    private static List<String> readInput() throws IOException {
        List<String> lines = Files.lines(Paths.get("src/day6/input.txt")).collect(Collectors.toList());

        List<String> groups = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        for (String line : lines) {
            if (line.length() == 0) {
                if (current.length() > 0) {
                    groups.add(current.toString());
                    current = new StringBuilder();
                }
            } else {
                current.append("\n").append(line);
            }
        }

        if (current.length() > 0) {
            groups.add(current.toString());
        }

        return groups;
    }
}
