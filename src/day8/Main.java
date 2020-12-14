package day8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    private static class Operation {
        String op;
        final Integer value;

        public Operation(String op, Integer value) {
            this.op = op;
            this.value = value;
        }
    }

    public static void main(String[] args) throws IOException {
        List<Operation> operations = readInput();

        run(operations, true);

        for (Operation operation : operations) {
            switch (operation.op) {
            case "nop":
                operation.op = "jmp";

                if (run(operations, false)) {
                    return;
                }

                operation.op = "nop";
                break;
            case "jmp":
                operation.op = "nop";

                if (run(operations, false)) {
                    return;
                }

                operation.op = "jmp";
                break;
            default:
                break;
            }
        }
    }

    private static boolean run(List<Operation> operations, boolean logLoop) {
        long accumulator = 0;
        Set<Integer> linesRan = new HashSet<>();
        int currentLine = 0;

        while (operations.size() > linesRan.size() && currentLine < operations.size()) {
            if (linesRan.contains(currentLine)) {
                if (logLoop) {
                    System.out.println("line " + (currentLine + 1) + " already ran");
                    System.out.println("accumulator: " + accumulator);
                }
                return false;
            }

            Operation o = operations.get(currentLine);
            linesRan.add(currentLine);

            switch (o.op) {
            case "nop" -> currentLine++;
            case "acc" -> {
                accumulator += o.value;
                currentLine++;
            }
            case "jmp" -> currentLine += o.value;
            }
        }

        System.out.println("accumulator: " + accumulator);
        return true;
    }

    private static List<Operation> readInput() throws IOException {
        List<String> lines = Files.lines(Paths.get("src/day8/input.txt")).collect(Collectors.toList());

        return lines.stream().map(line -> {
            Matcher matcher = Pattern.compile("^(acc|jmp|nop) ([+|-]\\d+)$").matcher(line);

            if (matcher.matches()) {
                return new Operation(matcher.group(1), Integer.parseInt(matcher.group(2)));
            } else {
                System.out.println("invalid operation: " + line);
                return null;
            }
        }).collect(Collectors.toList());
    }
}
