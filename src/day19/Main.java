package day19;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    private static final Pattern rulePattern = Pattern.compile("^(\\d+): (.+)$");
    private static final Pattern characterPattern = Pattern.compile("^\"([a-z])\"$");

    private static class Input {
        final Map<Integer, String> rules = new HashMap<>();
        final List<String> messages = new ArrayList<>();
    }

    public static void main(String[] args) throws IOException {
        Input input = readInput();

        // build regex
        String regex = "^" + buildRegex(input.rules.get(0), input.rules) + "$";
        Pattern pattern = Pattern.compile(regex);

        System.out.println(input.messages.stream().filter(message -> pattern.matcher(message).matches()).count());

        // part 2
        // 0: 8 11
        // 8: 42 | 42 8 => 8: 42+
        // 11: 42 31 | 42 11 31 => 11: 42{n} 31{n}
        // 0: 42{n+1,} 31{n}
        String regexRule42 = buildRegex(input.rules.get(42), input.rules);
        String regexRule31 = buildRegex(input.rules.get(31), input.rules);
        Set<String> matches = new HashSet<>();

        for (int i = 1; i < 5; ++i) {
            Pattern p = Pattern.compile("^(" + regexRule42 + "){" + (i + 1) + ",}(" + regexRule31 + "){" + i + "}$");
            input.messages.stream().filter(message -> !matches.contains(message)).forEach(message -> {
                if (p.matcher(message).matches()) {
                    matches.add(message);
                }
            });
        }
        System.out.println(matches.size());
    }

    private static String buildRegex(String currentRule, Map<Integer, String> allRules) {
        Matcher characterMatcher = characterPattern.matcher(currentRule);
        if (characterMatcher.matches()) {
            return characterMatcher.group(1);
        } else if (currentRule.contains("|")) {
            String[] parts = currentRule.split(" \\| ");

            return "(" + buildRegex(parts[0], allRules) + ")|(" + buildRegex(parts[1], allRules) + ")";
        } else {
            String[] parts = currentRule.split(" ");

            return Arrays.stream(parts).map(p -> "(" + buildRegex(allRules.get(Integer.parseInt(p)), allRules) + ")").collect(Collectors.joining());
        }
    }

    private static Input readInput() throws IOException {
        List<String> lines = Files.lines(Paths.get("src/day19/input.txt")).collect(Collectors.toList());

        boolean processingMessages = false;
        Input result = new Input();

        for (String line : lines) {
            if (line.isEmpty()) {
                processingMessages = true;
            } else if (processingMessages) {
                result.messages.add(line);
            } else {
                Matcher matcher = rulePattern.matcher(line);
                if (matcher.matches()) {
                    result.rules.put(Integer.parseInt(matcher.group(1)), matcher.group(2));
                } else {
                    System.out.println("invalid input line: " + line);
                }
            }
        }

        return result;
    }
}
