package day23;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static class Node {
        public Node next;
        public Integer label;

        public Node(Node next, Integer label) {
            this.next = next;
            this.label = label;
        }
    }

    public static void main(String[] args) throws IOException {
        List<Integer> input = readInput();

        // part 1
        // create linked list and hashmap from label to node
        Map<Integer, Node> labelMap = new HashMap<>();
        Node list = createList(input, labelMap);

        int numMoves = 100;
        Node currentCup = list;
        for (int i = 0; i < numMoves; ++i) {
            doMove(currentCup, labelMap);
            currentCup = currentCup.next;
        }

        printList(labelMap.get(1));

        // part 2
        labelMap.clear();
        List<Integer> paddedInput = new ArrayList<>(input);
        for (int i = input.size() + 1; i <= 1000000; ++i) {
            paddedInput.add(i);
        }
        list = createList(paddedInput, labelMap);

        numMoves = 10000000;
        currentCup = list;
        for (int i = 0; i < numMoves; ++i) {
            doMove(currentCup, labelMap);
            currentCup = currentCup.next;
        }

        Node cup1 = labelMap.get(1);
        System.out.println(BigInteger.valueOf(cup1.next.label).multiply(BigInteger.valueOf(cup1.next.next.label)));
    }

    private static void doMove(Node currentCup, final Map<Integer, Node> labelMap) {
        // pickup 3 cups next to current cup
        Node pickUp1 = currentCup.next;
        Node pickUp2 = pickUp1.next;
        Node pickUp3 = pickUp2.next;

        currentCup.next = pickUp3.next;

        // find destination node
        int destination = currentCup.label;
        do {
            destination--;
            if (destination == 0) {
                destination = labelMap.size();
            }
        } while (pickUp1.label == destination || pickUp2.label == destination || pickUp3.label == destination);

        Node destinationNode = labelMap.get(destination);
        Node tmp = destinationNode.next;
        destinationNode.next = pickUp1;
        pickUp3.next = tmp;

    }

    private static Node createList(List<Integer> elements, Map<Integer, Node> labelMap) {
        Node list = null;
        Node last = null;

        for (Integer label : elements) {
            Node n = new Node(list, label);
            labelMap.put(label, n);

            if (last != null) {
                last.next = n;
                n.next = list;
                last = n;
            } else {
                list = n;
                last = n;
                n.next = n;
            }
        }

        return list;
    }

    private static void printList(Node list) {
        Node iterator = list;
        do {
            System.out.print(iterator.label);
            iterator = iterator.next;
        } while (iterator != list);
        System.out.println();
    }

    private static List<Integer> readInput() throws IOException {
        List<String> lines = Files.lines(Paths.get("src/day23/input.txt")).collect(Collectors.toList());

        return lines.get(0)
                .chars()
                .mapToObj(i -> Integer.parseInt(String.valueOf((char) i)))
                .collect(Collectors.toList()
                );
    }
}
