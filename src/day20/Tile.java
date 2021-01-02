package day20;

import java.util.*;
import java.util.stream.Collectors;

public class Tile {
    private final long id;
    private List<List<Character>> data;

    private final Map<Direction, Tile> neighbors = new HashMap<>();

    public Tile(List<String> lines, List<List<Character>> data) {
        if (data != null) {
            this.id = 0;
            this.data = data;
        } else {
            String idLine = lines.get(0);
            idLine = idLine.replace("Tile ", "");
            idLine = idLine.substring(0, idLine.length() - 1);
            this.id = Long.parseLong(idLine);

            this.data = new ArrayList<>();
            lines.stream().skip(1).forEach(line -> this.data.add(line.chars().mapToObj(c -> (char) c).collect(Collectors.toList())));
        }
    }

    public long getId() {
        return this.id;
    }

    public List<List<Character>> getData() {
        return this.data;
    }

    public String getDataFlat() {
        return this.data.stream().map(row -> row.stream().map(Object::toString).collect(Collectors.joining())).collect(Collectors.joining());
    }

    public String getBorder(Direction direction) {
        return switch (direction) {
            case TOP -> getTopBorder();
            case BOTTOM -> getBottomBorder();
            case LEFT -> getLeftBorder();
            case RIGHT -> getRightBorder();
        };
    }

    public Tile getNeighbor(Direction direction) {
        return this.neighbors.get(direction);
    }

    public void setNeighbor(Direction direction, Tile tile) {
        this.neighbors.put(direction, tile);
    }

    public int getNumNeighbors() {
        return neighbors.size();
    }

    private String getTopBorder() {
        return data.get(0).stream().map(Object::toString).collect(Collectors.joining());
    }

    private String getBottomBorder() {
        return data.get(data.size() - 1).stream().map(Object::toString).collect(Collectors.joining());
    }

    private String getLeftBorder() {
        return data.stream().map(line -> line.get(0).toString()).collect(Collectors.joining());
    }

    private String getRightBorder() {
        return data.stream().map(line -> line.get(line.size() - 1).toString()).collect(Collectors.joining());
    }

    public void findNeighbors(Set<Tile> tiles) {
        if (neighbors.size() == Direction.values().length) {
            // all directions filled in
            return;
        }

        for (Tile t : tiles) {
            if (t.getId() == this.getId()) {
                continue;
            }

            for (Direction dir : Direction.values()) {
                if (this.neighbors.containsKey(dir)) {
                    // neighbor for direction already set
                    continue;
                }

                // get border at direction for this tile
                String border = this.getBorder(dir);
                String borderReverse = new StringBuilder(border).reverse().toString();


                // check other tile's borders
                for (Direction dir2 : Direction.values()) {
                    if (t.getNeighbor(dir2) != null) {
                        continue;
                    }

                    if (border.equals(t.getBorder(dir2)) || borderReverse.equals(t.getBorder(dir2))) {
                        // borders match
                        this.setNeighbor(dir, t);
                        t.setNeighbor(dir2, this);
                    }
                }
            }
        }
    }

    public void flipHorizontal() {
        // update data
        this.data.forEach(Collections::reverse);

        // update neighbors
        Tile leftNeighbor = this.getNeighbor(Direction.LEFT);
        this.setNeighbor(Direction.LEFT, this.getNeighbor(Direction.RIGHT));
        this.setNeighbor(Direction.RIGHT, leftNeighbor);
    }

    public void flipVertical() {
        // update data
        Collections.reverse(this.data);

        // update neighbors
        Tile topNeighbor = this.getNeighbor(Direction.TOP);
        this.setNeighbor(Direction.TOP, this.getNeighbor(Direction.BOTTOM));
        this.setNeighbor(Direction.BOTTOM, topNeighbor);
    }

    public void rotateRight() {
        // update data
        List<List<Character>> newData = new ArrayList<>();

        for (int i = 0; i < this.data.size(); ++i) {
            int finalI = i;
            newData.add(this.data.stream().map(row -> row.get(finalI)).collect(Collectors.toList()));
            Collections.reverse(newData.get(i));
        }
        this.data = newData;

        // update neighbors
        Tile topNeighbor = this.getNeighbor(Direction.TOP);
        Tile bottomNeighbor = this.getNeighbor(Direction.BOTTOM);
        Tile rightNeighbor = this.getNeighbor(Direction.RIGHT);
        this.setNeighbor(Direction.TOP, this.getNeighbor(Direction.LEFT));
        this.setNeighbor(Direction.RIGHT, topNeighbor);
        this.setNeighbor(Direction.BOTTOM, rightNeighbor);
        this.setNeighbor(Direction.LEFT, bottomNeighbor);
    }
}
