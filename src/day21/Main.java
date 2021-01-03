package day21;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static class Item {
        private final List<String> ingredients;
        private final List<String> allergens;

        public Item(String line) {
            String[] parts = line.split(" \\(contains ");
            ingredients = Arrays.asList(parts[0].split(" "));

            String allergens = parts[1].substring(0, parts[1].length() - 1);
            this.allergens = Arrays.asList(allergens.split(", "));
        }

        public List<String> getIngredients() {
            return ingredients;
        }

        public List<String> getAllergens() {
            return allergens;
        }
    }

    public static void main(String[] args) throws IOException {
        List<Item> input = readInput();
        Set<String> allergens = input.stream().flatMap(i -> i.getAllergens().stream()).collect(Collectors.toSet());

        Map<String, String> allergenIngredient = new HashMap<>();

        // match allergens to ingredients
        while (allergenIngredient.size() < allergens.size()) {
            Set<String> allergensLeft = allergens.stream()
                    .filter(a -> !allergenIngredient.containsKey(a))
                    .collect(Collectors.toSet());

            allergensLeft.forEach(allergen -> {
                List<Item> allergenItems = input.stream()
                        .filter(i -> i.getAllergens().contains(allergen)).collect(Collectors.toList());
                Set<String> possibleIngredients = allergenItems.stream()
                        .flatMap(i -> i.getIngredients().stream())
                        .filter(ingredient -> !allergenIngredient.containsValue(ingredient))
                        .filter(ingredient -> allergenItems.stream()
                                .allMatch(item -> item.getIngredients().contains(ingredient)))
                        .collect(Collectors.toSet());

                if (possibleIngredients.size() == 1) {
                    String ingredient = possibleIngredients.iterator().next();
                    allergenIngredient.put(allergen, ingredient);
                }
            });
        }

        System.out.println(input.stream()
                .flatMap(i -> i.getIngredients().stream())
                .filter(ingredient -> !allergenIngredient.containsValue(ingredient))
                .count());

        System.out.println(allergenIngredient.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.joining(",")));
    }

    private static List<Item> readInput() throws IOException {
        List<String> lines = Files.lines(Paths.get("src/day21/input.txt")).collect(Collectors.toList());

        return lines.stream().map(Item::new).collect(Collectors.toList());
    }
}
