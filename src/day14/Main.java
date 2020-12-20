package day14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    private static class Instruction {
        final String mask;
        final long address;
        final long value;

        public Instruction(String mask, long address, long value) {
            this.mask = mask;
            this.address = address;
            this.value = value;
        }
    }

    public static void main(String[] args) throws IOException {
        List<Instruction> instructions = readInput();

        part1(instructions);
        part2(instructions);
    }

    private static void part1(List<Instruction> instructions) {
        Map<Long, Long> memory = new HashMap<>();
        String currentMask = null;

        for (Instruction instruction : instructions) {
            if (instruction.mask != null) {
                currentMask = instruction.mask;
            } else {
                doOperationPart1(instruction, currentMask, memory);
            }
        }

        System.out.println(memory.values().stream().reduce(Long::sum).get());
    }

    private static void doOperationPart1(Instruction instruction, String mask, Map<Long, Long> memory) {
        String bits = numberToBits(instruction.value, mask.length());
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < mask.length(); ++i) {
            if (mask.charAt(i) == 'X' || mask.charAt(i) == 'x') {
                result.append(bits.charAt(i));
            } else {
                result.append(mask.charAt(i));
            }
        }

        memory.put(instruction.address, bitsToNumber(result.toString()));
    }

    private static void part2(List<Instruction> instructions) {
        Map<Long, Long> memory = new HashMap<>();
        String currentMask = null;

        for (Instruction instruction : instructions) {
            if (instruction.mask != null) {
                currentMask = instruction.mask;
            } else {
                doOperationPart2(instruction, currentMask, memory);
            }
        }

        System.out.println(memory.values().stream().reduce(Long::sum).get());
    }

    private static void doOperationPart2(Instruction instruction, String mask, Map<Long, Long> memory) {
        // calculate address
        String bits = numberToBits(instruction.address, mask.length());
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < mask.length(); ++i) {
            if (mask.charAt(i) == '0') {
                result.append(bits.charAt(i));
            } else if (mask.charAt(i) == '1') {
                result.append('1');
            } else {
                result.append('X');
            }
        }

        writeToMemory(result.toString(), instruction.value, memory);
    }

    private static void writeToMemory(String address, long value, Map<Long, Long> memory) {
        int firstXPos = address.indexOf('X');
        if (firstXPos == -1) {
            memory.put(bitsToNumber(address), value);
        } else {
            writeToMemory(address.substring(0, firstXPos) + "0" + address.substring(firstXPos + 1), value, memory);
            writeToMemory(address.substring(0, firstXPos) + "1" + address.substring(firstXPos + 1), value, memory);
        }
    }

    private static String numberToBits(long number, int numBits) {
        long remainder = number;
        StringBuilder result = new StringBuilder();

        for (int i = numBits - 1; i >= 0; i--) {
            long s = (long) Math.pow(2, i);

            if (remainder >= s) {
                remainder -= s;
                result.append("1");
            } else {
                result.append("0");
            }
        }

        return result.toString();
    }

    private static long bitsToNumber(String bits) {
        long result = 0;
        for (int i = 0; i < bits.length(); ++i) {
            if (bits.charAt(bits.length() - 1 - i) == '1') {
                result += Math.pow(2, i);
            }
        }

        return result;
    }

    private static List<Instruction> readInput() throws IOException {
        List<String> lines = Files.lines(Paths.get("src/day14/input.txt")).collect(Collectors.toList());

        Pattern maskPattern = Pattern.compile("^mask = ([X|01]{36})$");
        Pattern memPattern = Pattern.compile("^mem\\[(\\d+)] = (\\d+)$");

        return lines.stream().map(line -> {
            if (line.startsWith("mask")) {
                Matcher matcher = maskPattern.matcher(line);

                if (matcher.matches()) {
                    return new Instruction(matcher.group(1), 0, 0);
                }
            } else if (line.startsWith("mem")) {
                Matcher matcher = memPattern.matcher(line);

                if (matcher.matches()) {
                    return new Instruction(null, Long.parseLong(matcher.group(1)), Long.parseLong(matcher.group(2)));
                }
            }

            System.out.println("invalid command: " + line);

            return null;
        }).collect(Collectors.toList());
    }
}
