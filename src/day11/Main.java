package day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        List<List<Character>> input = readInput();

        while (doPassPart1(input)) ;
        System.out.println(occupiedCount(input));

        input = readInput();
        while (doPassPart2(input)) ;
        System.out.println(occupiedCount(input));
    }

    private static boolean doPassPart1(List<List<Character>> currentState) {
        boolean hasChanged = false;

        List<List<Character>> snapshot = currentState.stream().map(ArrayList::new).collect(Collectors.toList());

        for (int row = 0; row < currentState.size(); ++row) {
            for (int column = 0; column < currentState.get(row).size(); ++column) {
                char c = currentState.get(row).get(column);

                if (c == 'L' && getOccupiedAdjacentSeatsPart1(snapshot, row, column) == 0) {
                    currentState.get(row).set(column, '#');
                    hasChanged = true;
                } else if (c == '#' && getOccupiedAdjacentSeatsPart1(snapshot, row, column) >= 4) {
                    currentState.get(row).set(column, 'L');
                    hasChanged = true;
                }
            }
        }

        return hasChanged;
    }

    private static boolean doPassPart2(List<List<Character>> currentState) {
        boolean hasChanged = false;

        List<List<Character>> snapshot = currentState.stream().map(ArrayList::new).collect(Collectors.toList());

        for (int row = 0; row < currentState.size(); ++row) {
            for (int column = 0; column < currentState.get(row).size(); ++column) {
                char c = currentState.get(row).get(column);

                if (c == 'L' && getOccupiedVisibleSeatsPart2(snapshot, row, column) == 0) {
                    currentState.get(row).set(column, '#');
                    hasChanged = true;
                } else if (c == '#' && getOccupiedVisibleSeatsPart2(snapshot, row, column) >= 5) {
                    currentState.get(row).set(column, 'L');
                    hasChanged = true;
                }
            }
        }

        return hasChanged;
    }

    private static int getOccupiedAdjacentSeatsPart1(List<List<Character>> state, int row, int column) {
        int numOccupied = 0;
        for (int r = Math.max(row - 1, 0); r < Math.min(row + 2, state.size()); ++r) {
            for (int c = Math.max(column - 1, 0); c < Math.min(column + 2, state.get(0).size()); ++c) {
                if (row != r && column != c && state.get(r).get(c) == '#') {
                    numOccupied++;
                }
            }
        }

        return numOccupied;
    }

    private static int getOccupiedVisibleSeatsPart2(List<List<Character>> state, int row, int column) {
        int numOccupied = 0;
        int r;
        int c;

        // up
        for (r = row - 1; r >= 0; --r) {
            char ch = state.get(r).get(column);
            if (ch == '#') {
                numOccupied++;
                break;
            } else if (ch == 'L') {
                break;
            }
        }


        // up right
        r = row - 1;
        c = column + 1;
        while (r >= 0 && c < state.get(0).size()) {
            char ch = state.get(r).get(c);
            if (ch == '#') {
                numOccupied++;
                break;
            } else if (ch == 'L') {
                break;
            }
            r--;
            c++;
        }

        // right
        for (c = column + 1; c < state.get(0).size(); c++) {
            char ch = state.get(row).get(c);
            if (ch == '#') {
                numOccupied++;
                break;
            } else if (ch == 'L') {
                break;
            }
        }

        // down right
        r = row + 1;
        c = column + 1;
        while (r < state.size() && c < state.get(0).size()) {
            char ch = state.get(r).get(c);
            if (ch == '#') {
                numOccupied++;
                break;
            } else if (ch == 'L') {
                break;
            }
            r++;
            c++;
        }

        // down
        for (r = row + 1; r < state.size(); ++r) {
            char ch = state.get(r).get(column);
            if (ch == '#') {
                numOccupied++;
                break;
            } else if (ch == 'L') {
                break;
            }
        }

        // down left
        r = row + 1;
        c = column - 1;
        while (r < state.size() && c >= 0) {
            char ch = state.get(r).get(c);
            if (ch == '#') {
                numOccupied++;
                break;
            } else if (ch == 'L') {
                break;
            }
            r++;
            c--;
        }

        // left
        for (c = column - 1; c >= 0; c--) {
            char ch = state.get(row).get(c);
            if (ch == '#') {
                numOccupied++;
                break;
            } else if (ch == 'L') {
                break;
            }
        }

        // up left
        r = row - 1;
        c = column - 1;
        while (r >= 0 && c >= 0) {
            char ch = state.get(r).get(c);
            if (ch == '#') {
                numOccupied++;
                break;
            } else if (ch == 'L') {
                break;
            }
            r--;
            c--;
        }

        return numOccupied;
    }

    private static long occupiedCount(List<List<Character>> input) {
        return input.stream().map(row -> row.stream().filter(c -> c == '#').count()).reduce(0L, Long::sum);
    }

    private static List<List<Character>> readInput() throws IOException {
        List<String> lines = Files.lines(Paths.get("src/day11/input.txt")).collect(Collectors.toList());

        return lines.stream().map(line -> line.chars().mapToObj(c -> (char) c).collect(Collectors.toList())).collect(Collectors.toList());
    }
}
