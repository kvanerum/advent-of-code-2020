package day5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        List<BoardingPass> boardingPasses = readInput().stream().map(BoardingPass::new).collect(Collectors.toList());

        System.out.println(boardingPasses.stream().mapToInt(BoardingPass::getSeatID).max().getAsInt());

        List<Integer> seatIDs = boardingPasses.stream().map(BoardingPass::getSeatID).collect(Collectors.toList());
        System.out.println(seatIDs.stream().filter(seatID -> seatIDs.contains(seatID + 2) && !seatIDs.contains(
                seatID + 1)).findFirst().get() + 1);
    }

    private static List<String> readInput() throws IOException {
        return Files.lines(Paths.get("src/day5/input.txt")).collect(Collectors.toList());
    }
}
