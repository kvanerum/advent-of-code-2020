package day17;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class HyperCube {
    private static class Position {
        private final List<Integer> p;

        public Position(Integer[] position) {
            p = Arrays.asList(position);
        }

        public Position(List<Integer> position) {
            p = new ArrayList<>(position);
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof Position) || p.size() != ((Position) o).p.size()) {
                return false;
            }

            for (int i = 0; i < p.size(); ++i) {
                if (!p.get(i).equals(((Position) o).p.get(i))) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public int hashCode() {
            return p.hashCode();
        }

        @Override
        public String toString() {
            return "(" + p.stream().map(Object::toString).collect(Collectors.joining(",")) + ")";
        }
    }

    private final Map<Position, Character> values = new HashMap<>();
    private final List<Set<Integer>> dimensions = new ArrayList<>();

    public HyperCube(int dimension, List<List<Character>> initial) {
        for (int d = 0; d < dimension; ++d) {
            dimensions.add(new HashSet<>(Collections.singletonList(0)));
        }

        for (int i = 0; i < initial.size(); ++i) {
            dimensions.get(0).add(i);
            for (int j = 0; j < initial.get(i).size(); ++j) {
                dimensions.get(1).add(j);
                Integer[] pos = new Integer[dimensions.size()];
                Arrays.fill(pos, 0);
                pos[0] = i;
                pos[1] = j;
                values.put(new Position(pos), initial.get(i).get(j));
            }
        }
    }

    public long activeCount() {
        long result = 0;

        for (Character c : values.values()) {
            if (c.equals('#')) {
                result++;
            }
        }

        return result;
    }

    public void doCycle() {
        // take snapshot
        Map<Position, Character> snapshot = getSnapshot();

        addDimensions();

        // loop through all cubes
        checkCells(dimensions, new ArrayList<>(), snapshot);
    }

    private void checkCells(List<Set<Integer>> dimensionsLeft, List<Integer> currentPosition, Map<Position, Character> snapshot) {
        if (dimensionsLeft.isEmpty()) {
            updateCell(currentPosition.toArray(new Integer[0]), snapshot);
        } else {
            Set<Integer> currentDimension = dimensionsLeft.get(0);
            List<Set<Integer>> newDimensionsLeft = dimensionsLeft.stream().skip(1).collect(Collectors.toList());

            currentDimension.forEach(index -> {
                List<Integer> newPosition = new ArrayList<>(currentPosition);
                newPosition.add(index);
                checkCells(newDimensionsLeft, newPosition, snapshot);
            });
        }
    }

    private void updateCell(Integer[] position, Map<Position, Character> snapshot) {
        // count active neighbours
        long activeNeighbours = countActiveNeighbours(position, Arrays.asList(position), 0, snapshot);

        if (isActive(position, snapshot) && activeNeighbours != 2 && activeNeighbours != 3) {
            // set inactive
            values.put(new Position(position), '.');
        } else if (!isActive(position, snapshot) && activeNeighbours == 3) {
            // set active
            values.put(new Position(position), '#');
        }
    }

    private long countActiveNeighbours(Integer[] originalPosition, List<Integer> currentPosition, int currentDimension, Map<Position, Character> snapshot) {
        if (currentDimension >= currentPosition.size()) {
            if (new Position(originalPosition).equals(new Position(currentPosition)) || !isActive(currentPosition.toArray(new Integer[originalPosition.length]),
                    snapshot)) {
                return 0;
            } else {
                return 1;
            }
        } else {
            long result = 0;

            List<Integer> positionPrev = new ArrayList<>(currentPosition);
            positionPrev.set(currentDimension, currentPosition.get(currentDimension) - 1);

            List<Integer> positionNext = new ArrayList<>(currentPosition);
            positionNext.set(currentDimension, currentPosition.get(currentDimension) + 1);

            result += countActiveNeighbours(originalPosition, positionPrev, currentDimension + 1, snapshot);
            result += countActiveNeighbours(originalPosition, currentPosition, currentDimension + 1, snapshot);
            result += countActiveNeighbours(originalPosition, positionNext, currentDimension + 1, snapshot);
            return result;
        }
    }

    private boolean isActive(Integer[] position, Map<Position, Character> state) {
        Character c = state.get(new Position(position));

        return c != null && c.equals('#');
    }

    private void addDimensions() {
        dimensions.forEach(dimension -> {
            int min = dimension.stream().min(Integer::compare).get();
            int max = dimension.stream().max(Integer::compare).get();

            dimension.add(min - 1);
            dimension.add(max + 1);
        });
    }

    private Map<Position, Character> getSnapshot() {
        return values.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
