package day18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    private static final Pattern numberPattern = Pattern.compile("^\\d+$");
    private static final Pattern operationPattern = Pattern.compile("^(\\d+) ([+*]) (\\d+)(.*)$");
    private static final Pattern additionPattern = Pattern.compile("^(.*?)(\\d+) \\+ (\\d+)(.*?)$");
    private static final Pattern multiplicationPattern = Pattern.compile("^(\\d+) \\* (\\d+)(.*)$");

    public static void main(String[] args) throws IOException {
        List<String> input = readInput();

        System.out.println(input.stream().map(Main::solvePart1).reduce(Long::sum).get());
        System.out.println(input.stream().map(Main::solvePart2).reduce(Long::sum).get());
    }

    private static long solvePart1(String input) {
        if (numberPattern.matcher(input).matches()) {
            return Long.parseLong(input);
        } else if (input.contains("(")) {
            int beginPos = input.indexOf("(");
            int numParentheses = 1;
            int currentPos = beginPos;
            while (numParentheses != 0) {
                currentPos++;
                if (input.charAt(currentPos) == '(') {
                    numParentheses++;
                } else if (input.charAt(currentPos) == ')') {
                    numParentheses--;
                }
            }

            return solvePart1(input.substring(0, beginPos) + solvePart1(input.substring(beginPos + 1, currentPos)) +
                              input.substring(currentPos + 1));
        } else {
            Matcher operationMatcher = operationPattern.matcher(input);
            if (operationMatcher.matches()) {
                long left = Long.parseLong(operationMatcher.group(1));
                long right = Long.parseLong(operationMatcher.group(3));
                long replacement = operationMatcher.group(2).equals("+") ? left + right : left * right;

                return solvePart1(replacement + operationMatcher.group(4));
            } else {
                System.out.println("invalid input " + input);
            }
        }

        return 0;
    }

    private static long solvePart2(String input) {
        if (numberPattern.matcher(input).matches()) {
            return Long.parseLong(input);
        } else if (input.contains("(")) {
            int beginPos = input.indexOf("(");
            int numParentheses = 1;
            int currentPos = beginPos;
            while (numParentheses != 0) {
                currentPos++;
                if (input.charAt(currentPos) == '(') {
                    numParentheses++;
                } else if (input.charAt(currentPos) == ')') {
                    numParentheses--;
                }
            }

            return solvePart2(input.substring(0, beginPos) + solvePart2(input.substring(beginPos + 1, currentPos)) +
                              input.substring(currentPos + 1));
        } else if (input.contains("+")) {
            Matcher additionMatcher = additionPattern.matcher(input);
            additionMatcher.matches();

            long left = Long.parseLong(additionMatcher.group(2));
            long right = Long.parseLong(additionMatcher.group(3));
            long replacement = left + right;

            return solvePart2(additionMatcher.group(1) + replacement + additionMatcher.group(4));
        } else {
            Matcher multiplicationMatcher = multiplicationPattern.matcher(input);
            multiplicationMatcher.matches();

            long left = Long.parseLong(multiplicationMatcher.group(1));
            long right = Long.parseLong(multiplicationMatcher.group(2));
            long replacement = left * right;

            return solvePart2(replacement + multiplicationMatcher.group(3));
        }
    }

    private static List<String> readInput() throws IOException {
        return Files.lines(Paths.get("src/day18/input.txt")).collect(Collectors.toList());
    }
}
