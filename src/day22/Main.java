package day22;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    private static class Input {
        public final List<Integer> player1 = new LinkedList<>();
        public final List<Integer> player2 = new LinkedList<>();
    }

    private static class GameResult {
        public final int winner;
        public final List<Integer> winnerCards;

        public GameResult(int winner, List<Integer> cards) {
            this.winner = winner;
            this.winnerCards = cards;
        }
    }

    public static void main(String[] args) throws IOException {
        Input input = readInput();

        combat(new LinkedList<>(input.player1), new LinkedList<>(input.player2));
        GameResult result = recursiveCombat(new LinkedList<>(input.player1), new LinkedList<>(input.player2));
        System.out.println(calculateScore(result.winnerCards));
    }

    private static void combat(List<Integer> player1, List<Integer> player2) {
        // play
        while (player1.size() > 0 && player2.size() > 0) {
            Integer p1 = player1.remove(0);
            Integer p2 = player2.remove(0);

            if (p1 > p2) {
                player1.add(p1);
                player1.add(p2);
            } else {
                player2.add(p2);
                player2.add(p1);
            }
        }

        List<Integer> winner = player1.size() == 0 ? player2 : player1;
        System.out.println(calculateScore(winner));
    }

    private static GameResult recursiveCombat(List<Integer> player1, List<Integer> player2) {
        Set<String> player1Configurations = new HashSet<>();
        Set<String> player2Configurations = new HashSet<>();

        // play
        while (player1.size() > 0 && player2.size() > 0) {
            Integer p1 = player1.remove(0);
            Integer p2 = player2.remove(0);

            int winner;

            if (player1.size() >= p1 && player2.size() >= p2) {
                GameResult r = recursiveCombat(new LinkedList<>(player1.subList(0, p1)), new LinkedList<>(player2.subList(0, p2)));
                winner = r.winner;
            } else {
                winner = p1 > p2 ? 1 : 2;
            }

            if (winner == 1) {
                player1.add(p1);
                player1.add(p2);
            } else {
                player2.add(p2);
                player2.add(p1);
            }

            if (player1Configurations.contains(serializeDeck(player1)) || player2Configurations.contains(serializeDeck(player2))) {
                return new GameResult(1, player1);
            }

            // add configurations
            player1Configurations.add(serializeDeck(player1));
            player2Configurations.add(serializeDeck(player2));
        }

        if (player1.size() > 0) {
            return new GameResult(1, player1);
        } else {
            return new GameResult(2, player2);
        }
    }

    private static long calculateScore(List<Integer> input) {
        return IntStream.range(0, input.size())
                .mapToLong(i -> (long) (input.size() - i) * input.get(i))
                .reduce(Long::sum)
                .getAsLong();
    }

    private static String serializeDeck(List<Integer> deck) {
        return deck.stream().map(Object::toString).collect(Collectors.joining());
    }

    private static Input readInput() throws IOException {
        List<String> lines = Files.lines(Paths.get("src/day22/input.txt")).collect(Collectors.toList());

        Input result = new Input();
        boolean player2 = false;

        for (String line : lines) {
            if (line.startsWith("Player")) {
                continue;
            } else if (line.isEmpty()) {
                player2 = true;
            } else {
                Integer i = Integer.parseInt(line);

                if (player2) {
                    result.player2.add(i);
                } else {
                    result.player1.add(i);
                }
            }
        }

        return result;
    }
}
