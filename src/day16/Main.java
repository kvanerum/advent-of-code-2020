package day16;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    private static class Input {
        final Map<String, List<Rule>> rules;
        List<Integer> yourTicket;
        final List<List<Integer>> nearbyTickets;

        Input() {
            rules = new HashMap<>();
            yourTicket = new ArrayList<>();
            nearbyTickets = new ArrayList<>();
        }
    }

    private static class Rule {
        final int from;
        final int until;

        public Rule(int from, int until) {
            this.from = from;
            this.until = until;
        }
    }

    public static void main(String[] args) throws IOException {
        Input input = readInput();

        long error = 0;

        // check fields
        List<Rule> allRules = input.rules.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        for (List<Integer> ticket : input.nearbyTickets) {
            for (Integer field : ticket) {
                if (!isValidField(field, allRules)) {
                    error += field;
                }
            }
        }

        System.out.println(error);

        List<List<Integer>> validTickets = input.nearbyTickets.stream().filter(ticket -> ticket.stream().allMatch(field -> isValidField(field, allRules)))
                .collect(Collectors.toList());

        // for each rule, find the field indices that apply
        Map<String, List<Integer>> rulesIndices = input.rules.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, rule -> {
            List<Integer> result = new ArrayList<>();
            for (int index = 0; index < input.rules.size(); ++index) {
                if (isRuleValidForAllTickets(rule.getValue(), index, validTickets)) {
                    result.add(index);
                }
            }

            return result;
        }));

        Set<Integer> indicesToAssign = IntStream.range(0, input.rules.size()).boxed().collect(Collectors.toSet());
        Map<String, Integer> rulesIndex = new HashMap<>();

        while (!indicesToAssign.isEmpty()) {
            // find a rule with only 1 matching field index
            Map.Entry<String, List<Integer>> first = rulesIndices.entrySet().stream().filter(r -> r.getValue().size() == 1).findFirst().get();
            Integer index = first.getValue().get(0);
            rulesIndex.put(first.getKey(), index);

            // remove index from all rules
            rulesIndices.values().forEach(x -> x.remove(index));
            indicesToAssign.remove(index);
        }

        System.out.println(rulesIndex);

        System.out.println(
                rulesIndex.entrySet().stream().filter(entry -> entry.getKey().startsWith("departure"))
                        .map(rule -> BigInteger.valueOf(input.yourTicket.get(rule.getValue())))
                        .reduce(BigInteger.ONE, BigInteger::multiply));
    }

    private static boolean isValidField(Integer field, List<Rule> rules) {
        return rules.stream().anyMatch(rule -> field >= rule.from && field <= rule.until);
    }

    private static boolean isRuleValidForAllTickets(List<Rule> rules, int index, List<List<Integer>> tickets) {
        return tickets.stream().allMatch(ticket -> isValidField(ticket.get(index), rules));
    }

    private static Input readInput() throws IOException {
        List<String> lines = Files.lines(Paths.get("src/day16/input.txt")).collect(Collectors.toList());
        Input result = new Input();

        Pattern rulePattern = Pattern.compile("^([a-z\\s]+): (\\d+)-(\\d+) or (\\d+)-(\\d+)$");

        lines.forEach(line -> {
            if (line.isEmpty() || line.equals("your ticket:") || line.equals("nearby tickets:")) {
                return;
            }

            Matcher m = rulePattern.matcher(line);

            if (m.matches()) {
                List<Rule> rules = new ArrayList<>();
                rules.add(new Rule(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3))));
                rules.add(new Rule(Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5))));
                result.rules.put(m.group(1), rules);
            } else {
                result.nearbyTickets.add(Arrays.stream(line.split(",")).map(Integer::parseInt).collect(Collectors.toList()));
            }
        });

        // first ticket is yourTicket
        result.yourTicket = result.nearbyTickets.remove(0);

        return result;
    }
}
