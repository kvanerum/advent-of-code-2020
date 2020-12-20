package day12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    private static final Pattern pattern = Pattern.compile("^([NSEWLRF])(\\d+)$");

    private static int horizontalPos = 0;
    private static int verticalPos = 0;
    private static int heading = 90; // 0 = north, 90 = east, 180 = south, 270 = west
    private static int wpHorizontalPos = 0;
    private static int wpVerticalPos = 0;

    public static void main(String[] args) throws IOException {
        List<String> commands = readInput();

        commands.forEach(Main::doCommand);
        System.out.println(distance());

        horizontalPos = 0;
        verticalPos = 0;
        heading = 90;
        wpHorizontalPos = 10;
        wpVerticalPos = 1;

        commands.forEach(Main::doCommand2);
        System.out.println(distance());
    }

    private static void doCommand(String command) {
        Matcher matcher = pattern.matcher(command);

        if (matcher.matches()) {
            String action = matcher.group(1);
            int value = Integer.parseInt(matcher.group(2));

            switch (action) {
                case "N":
                    verticalPos -= value;
                    break;
                case "S":
                    verticalPos += value;
                    break;
                case "E":
                    horizontalPos += value;
                    break;
                case "W":
                    horizontalPos -= value;
                    break;
                case "L":
                    heading = (heading - value + 360) % 360;
                    break;
                case "R":
                    heading = (heading + value + 360) % 360;
                    break;
                case "F":
                    switch (heading) {
                        case 0 -> verticalPos -= value;
                        case 90 -> horizontalPos += value;
                        case 180 -> verticalPos += value;
                        case 270 -> horizontalPos -= value;
                        default -> System.out.println("invalid heading: " + heading);
                    }
                    break;
                default:
                    System.out.println("invalid command: " + command);
                    break;
            }
        } else {
            System.out.println("invalid command: " + command);
        }
    }

    private static void doCommand2(String command) {
        Matcher matcher = pattern.matcher(command);

        if (matcher.matches()) {
            String action = matcher.group(1);
            int value = Integer.parseInt(matcher.group(2));

            switch (action) {
                case "N":
                    wpVerticalPos += value;
                    break;
                case "S":
                    wpVerticalPos -= value;
                    break;
                case "E":
                    wpHorizontalPos += value;
                    break;
                case "W":
                    wpHorizontalPos -= value;
                    break;
                case "L":
                    int temp;
                    switch (value) {
                        case 90 -> {
                            temp = wpHorizontalPos;
                            wpHorizontalPos = -wpVerticalPos;
                            wpVerticalPos = temp;
                        }
                        case 180 -> {
                            wpHorizontalPos = -wpHorizontalPos;
                            wpVerticalPos = -wpVerticalPos;
                        }
                        case 270 -> {
                            temp = wpHorizontalPos;
                            wpHorizontalPos = wpVerticalPos;
                            wpVerticalPos = -temp;
                        }
                        default -> System.out.println("invalid command " + command);
                    }
                    break;
                case "R":
                    switch (value) {
                        case 90 -> {
                            temp = wpHorizontalPos;
                            wpHorizontalPos = wpVerticalPos;
                            wpVerticalPos = -temp;
                        }
                        case 180 -> {
                            wpHorizontalPos = -wpHorizontalPos;
                            wpVerticalPos = -wpVerticalPos;
                        }
                        case 270 -> {
                            temp = wpHorizontalPos;
                            wpHorizontalPos = -wpVerticalPos;
                            wpVerticalPos = temp;
                        }
                        default -> System.out.println("invalid command " + command);
                    }
                    break;
                case "F":
                    horizontalPos += wpHorizontalPos * value;
                    verticalPos += wpVerticalPos * value;
                    break;
                default:
                    System.out.println("invalid command: " + command);
                    break;
            }
        } else {
            System.out.println("invalid command: " + command);
        }
    }

    private static int distance() {
        return Math.abs(horizontalPos) + Math.abs(verticalPos);
    }

    private static List<String> readInput() throws IOException {
        return Files.lines(Paths.get("src/day12/input.txt")).collect(Collectors.toList());
    }
}
