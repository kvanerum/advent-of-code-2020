package day20;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Tile {
    private final long id;
    private final List<List<Character>> data;

    private Tile topNeighbor;
    private Tile BottomNeighbor;
    private Tile leftNeighbor;
    private Tile rightNeighbor;

    public Tile(List<String> lines) {
        String idLine = lines.get(0);
        idLine = idLine.replace("Tile ", "");
        idLine = idLine.substring(0, idLine.length() - 1);
        this.id = Long.parseLong(idLine);

        this.data = new ArrayList<>();
        lines.stream().skip(1).forEach(line -> data.add(line.chars().mapToObj(c -> (char) c).collect(Collectors.toList())));
    }

    public long getId() {
        return this.id;
    }

    public String getTopBorder() {
        return data.get(0).stream().map(c -> c.toString()).collect(Collectors.joining());
    }

    public String getBottomBorder() {
        return data.get(data.size() - 1).stream().map(c -> c.toString()).collect(Collectors.joining());
    }

    public String getLeftBorder() {
        return data.stream().map(line -> line.get(0).toString()).collect(Collectors.joining());
    }

    public String getRightBorder() {
        return data.stream().map(line -> line.get(line.size() - 1).toString()).collect(Collectors.joining());
    }

    public Tile getTopNeighbor() {
        return topNeighbor;
    }

    public void setTopNeighbor(Tile topNeighbor) {
        this.topNeighbor = topNeighbor;
    }

    public Tile getBottomNeighbor() {
        return BottomNeighbor;
    }

    public void setBottomNeighbor(Tile bottomNeighbor) {
        BottomNeighbor = bottomNeighbor;
    }

    public Tile getLeftNeighbor() {
        return leftNeighbor;
    }

    public void setLeftNeighbor(Tile leftNeighbor) {
        this.leftNeighbor = leftNeighbor;
    }

    public Tile getRightNeighbor() {
        return rightNeighbor;
    }

    public void setRightNeighbor(Tile rightNeighbor) {
        this.rightNeighbor = rightNeighbor;
    }

    public void findNeighbors(List<Tile> tiles) {
        for (Tile other : tiles) {
            if (other.getId() == this.getId()) {
                continue;
            }

            if (this.getLeftNeighbor() == null) {

            }
        }
    }

    @Override
    public String toString() {
        return "Tile{" +
                "id=" + id +
                ", data=" + data +
                '}';
    }
}
