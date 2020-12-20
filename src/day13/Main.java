package day13;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main {
    private static class Input {
        public final int earliestTimestamp;
        public final List<Integer> busses;

        public Input(int earliestTimestamp, List<Integer> busses) {
            this.earliestTimestamp = earliestTimestamp;
            this.busses = busses;
        }
    }

    public static void main(String[] args) throws IOException {
        Input input = readInput();

        int firstBus = input.busses.stream()
                .filter(Objects::nonNull).min(Comparator.comparingInt(a -> (a -
                                                                            input.earliestTimestamp %
                                                                            a)))
                .get();

        System.out.println(firstBus * (firstBus - input.earliestTimestamp % firstBus));

        // part 2
        long t = 0;
        long multiplier = input.busses.stream().filter(Objects::nonNull).findFirst().get();

        for (int index = 1; index < input.busses.size(); ++index) {
            Integer current = input.busses.get(index);

            if (current != null) {
                //  find new t with current multiplier (new t must always be a multiply of current multiplier)
                int j = 0;
                while ((t + (j * multiplier) + index) % current != 0) {
                    j++;
                }

                t += j * multiplier;

                // find new multiplier
                // new multiplier must also be a multiply of the current multiplier
                j = 1;
                while ((t + (j * multiplier) + index) % current != 0) {
                    j++;
                }

                multiplier *= j;
            }
        }

        System.out.println(t);
    }

    private static Input readInput() throws IOException {
        List<String> lines = Files.lines(Paths.get("src/day13/input.txt")).collect(Collectors.toList());

        return new Input(Integer.parseInt(lines.get(0)), Arrays.stream(lines.get(1).split(","))
                .map(i -> i.equals("x") ? null : Integer.parseInt(i))
                .collect(Collectors.toList()));
    }
}
