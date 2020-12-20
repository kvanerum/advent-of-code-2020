package day7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        Map<String, Map<String, Integer>> rules = readInput();

        Set<String> containers = findContainers("shiny gold", rules);
        System.out.println(containers.size());

        long bagContents = findBagContentsCount("shiny gold", rules) - 1; //remove "main" bag
        System.out.println(bagContents);
    }

    private static Set<String> findContainers(String color, Map<String, Map<String, Integer>> rules) {
        Set<String> result = new HashSet<>();
        result.add(color); // add color to start algorithm
        int currentSize;

        do {
            currentSize = result.size();

            // add possible containers to result set
            result.addAll(rules.entrySet().stream().filter(rule -> !Collections.disjoint(rule.getValue().keySet(), result)).map(Map.Entry::getKey)
                    .collect(Collectors.toSet()));

        } while (result.size() > currentSize);

        result.remove(color); // remove color

        return result;
    }

    private static long findBagContentsCount(String color, Map<String, Map<String, Integer>> rules) {
        return rules.get(color).entrySet().stream().map(bag -> bag.getValue() * findBagContentsCount(bag.getKey(), rules)).reduce(1L, Long::sum);
    }

    private static void parseContents(String contents, Map<String, Integer> map) {
        Pattern pattern = Pattern.compile("(\\d+) ([\\w\\s]*) bag[s]?[, ]?+");
        Matcher matcher = pattern.matcher(contents);

        while (matcher.find()) {
            int count = Integer.parseInt(matcher.group(1));
            String color = matcher.group(2);
            map.putIfAbsent(color, count);
        }
    }

    private static Map<String, Map<String, Integer>> readInput() throws IOException {
        List<String> lines = Files.lines(Paths.get("src/day7/input.txt")).collect(Collectors.toList());

        Map<String, Map<String, Integer>> rules = new HashMap<>();
        lines.forEach(line -> {
            Pattern pattern = Pattern.compile("^(.*) bags contain ([no other bags]|[\\d+ \\w\\s* bags?, ?]+)\\.$");
            Matcher matcher = pattern.matcher(line);

            if (matcher.matches()) {
                String color = matcher.group(1);
                String contents = matcher.group(2);
                Map<String, Integer> contentsMap = new HashMap<>();
                if (!contents.equals("no other bags")) {
                    parseContents(contents, contentsMap);
                }

                rules.put(color, contentsMap);
            } else {
                System.out.println("invalid rule");
            }
        });

        return rules;
    }
}
