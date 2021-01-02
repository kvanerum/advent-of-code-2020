package day20;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        Set<Tile> tiles = readInput();

        // find neighbors for each tile
        tiles.forEach(tile -> tile.findNeighbors(tiles));

        System.out.println(tiles.stream().filter(tile -> tile.getNumNeighbors() == 2).map(Tile::getId).reduce(1L, (a, b) -> a * b));

        List<List<Tile>> grid = toGrid(tiles);
        List<List<Character>> flatGrid = flatten(grid);
        Tile image = new Tile(null, flatGrid);

        // find sea monsters
        //                  #
        //#    ##    ##    ###
        // #  #  #  #  #  #

        String regex1 = ".{18}(#).";
        String regex2 = "(#).{4}(#)(#).{4}(#)(#).{4}(#)(#)(#)";
        String regex3 = ".(#).{2}(#).{2}(#).{2}(#).{2}(#).{2}(#).{3}";
        String padding = ".{" + (flatGrid.size() - 20) + "}";
        String regex = regex1 + padding + regex2 + padding + regex3;
        Pattern pattern = Pattern.compile(regex);

        Set<String> variants = new HashSet<>();

        for (int i = 0; i < 4; ++i) {
            variants.add(image.getDataFlat());

            // flip horizontal
            image.flipHorizontal();
            variants.add(image.getDataFlat());
            image.flipHorizontal();

            // flip vertical
            image.flipVertical();
            variants.add(image.getDataFlat());

            // flip vertical + horizontal
            image.flipHorizontal();
            variants.add(image.getDataFlat());

            // reset
            image.flipHorizontal();
            image.flipVertical();

            image.rotateRight();
        }

        String v = variants.stream().filter(variant -> variant.matches(".*" + regex + ".*")).findFirst().orElseThrow();
        StringBuilder sb = new StringBuilder(v);

        Matcher matcher = pattern.matcher(v);
        int from = 0;
        while (matcher.find(from)) {
            for (int i = 0; i < matcher.groupCount(); ++i) {
                sb = sb.replace(matcher.start(i + 1), matcher.end(i + 1), "0");
            }

            from = matcher.start() + 1;
        }

        v = sb.toString();

        long roughness = v.chars().mapToObj(c -> (char) c).filter(c -> c == '#').count();
        System.out.println(roughness);
    }

    private static List<List<Tile>> toGrid(Set<Tile> tiles) {
        // find first corner, this will become the top-left corner
        Tile currentLeftMost = tiles.stream().filter(tile -> tile.getNumNeighbors() == 2).findFirst().get();

        // rotate until no top and left neighbors
        while (currentLeftMost.getNeighbor(Direction.TOP) != null || currentLeftMost.getNeighbor(Direction.LEFT) != null) {
            currentLeftMost.rotateRight();
        }

        List<List<Tile>> grid = new ArrayList<>();

        do {
            List<Tile> currentRow = new ArrayList<>();
            grid.add(currentRow);

            Tile rowIterator = currentLeftMost;
            do {
                currentRow.add(rowIterator);
                if (rowIterator.getNeighbor(Direction.RIGHT) == null) {
                    break;
                }

                // rotate right neighbor until it matches
                while (rowIterator.getNeighbor(Direction.RIGHT).getNeighbor(Direction.LEFT) == null || rowIterator.getNeighbor(Direction.RIGHT).getNeighbor(Direction.LEFT).getId() != rowIterator.getId()) {
                    rowIterator.getNeighbor(Direction.RIGHT).rotateRight();
                }

                // flip horizontal if borders don't match
                if (!rowIterator.getBorder(Direction.RIGHT).equals(rowIterator.getNeighbor(Direction.RIGHT).getBorder(Direction.LEFT))) {
                    rowIterator.getNeighbor(Direction.RIGHT).flipVertical();
                }

                rowIterator = rowIterator.getNeighbor(Direction.RIGHT);
            } while (true);

            // find next row
            if (currentLeftMost.getNeighbor(Direction.BOTTOM) == null) {
                break;
            }

            // rotate bottom neighbor until it matches
            while (currentLeftMost.getNeighbor(Direction.BOTTOM).getNeighbor(Direction.TOP) == null || currentLeftMost.getNeighbor(Direction.BOTTOM).getNeighbor(Direction.TOP).getId() != currentLeftMost.getId()) {
                currentLeftMost.getNeighbor(Direction.BOTTOM).rotateRight();
            }

            // flip vertical if borders don't match
            if (!currentLeftMost.getBorder(Direction.BOTTOM).equals(currentLeftMost.getNeighbor(Direction.BOTTOM).getBorder(Direction.TOP))) {
                currentLeftMost.getNeighbor(Direction.BOTTOM).flipHorizontal();
            }

            currentLeftMost = currentLeftMost.getNeighbor(Direction.BOTTOM);
        } while (true);

        return grid;
    }

    private static List<List<Character>> flatten(List<List<Tile>> grid) {
        List<List<Character>> result = new ArrayList<>();
        int tileSize = grid.get(0).get(0).getData().size();

        for (List<Tile> tiles : grid) {
            for (int i = 1; i < tileSize - 1; ++i) {
                List<Character> resultRow = new ArrayList<>();

                for (int column = 0; column < tiles.size(); ++column) {
                    for (int j = 1; j < tileSize - 1; ++j) {
                        resultRow.add(tiles.get(column).getData().get(i).get(j));
                    }
                }

                result.add(resultRow);
            }
        }

        return result;
    }

    private static Set<Tile> readInput() throws IOException {
        List<String> lines = Files.lines(Paths.get("src/day20/input.txt")).collect(Collectors.toList());

        Set<Tile> result = new HashSet<>();
        int fromIndex = 0;

        for (int i = 1; i < lines.size(); ++i) {
            if (lines.get(i).isEmpty()) {
                result.add(new Tile(lines.subList(fromIndex, i), null));
                fromIndex = i + 1;
            }
        }

        // add last one
        result.add(new Tile(lines.subList(fromIndex, lines.size()), null));

        return result;
    }
}
