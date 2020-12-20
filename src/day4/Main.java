package day4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        List<String> input = readInput();

        System.out.println(
                input.stream().filter(
                        passport -> isValidPassportPart1(passport,
                                Arrays.asList("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"))).count());

        System.out.println(input.stream().filter(
                passport -> isValidYear(passport, "byr", 1920, 2002) && isValidYear(passport, "iyr", 2010,
                        2020) && isValidYear(passport, "eyr", 2020, 2030) && isValidHeight(
                        passport) && isValidHairColor(passport) && isValidEyeColor(passport) && isValidPassportId(
                        passport)).count());
    }

    private static boolean isValidPassportPart1(String passport, List<String> requiredFields) {
        return requiredFields.stream().allMatch(field -> passportHasField(passport, field));
    }

    private static boolean passportHasField(String passport, String field) {
        return passport.matches(".*" + field + ":.+\\s?.*");
    }

    private static boolean isValidYear(String passport, String field, int min, int max) {
        Pattern pattern = Pattern.compile(".*" + field + ":(\\d{4}?).*");
        Matcher matcher = pattern.matcher(passport);

        if (matcher.matches()) {
            int year = Integer.parseInt(matcher.group(1));
            return year >= min && year <= max;
        }

        return false;
    }

    private static boolean isValidHeight(String passport) {
        Pattern pattern = Pattern.compile(".*hgt:(\\d+)(in|cm).*");
        Matcher matcher = pattern.matcher(passport);

        if (matcher.matches()) {
            int value = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2);

            return unit.equals("cm") ? value >= 150 && value <= 193 : value >= 59 && value <= 76;
        }

        return false;
    }

    private static boolean isValidHairColor(String passport) {
        return passport.matches(".*hcl:#[0-9a-f]{6}?.*");
    }

    private static boolean isValidEyeColor(String passport) {
        return passport.matches(".*ecl:(amb|blu|brn|gry|grn|hzl|oth).*");
    }

    private static boolean isValidPassportId(String passport) {
        return passport.matches(".*pid:\\d{9}+(\\D|\\s|$).*");
    }

    private static List<String> readInput() throws IOException {
        List<String> lines = Files.lines(Paths.get("src/day4/input.txt")).collect(Collectors.toList());

        List<String> passports = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        for (String line : lines) {
            line = line.trim();

            if (line.length() == 0) {
                if (current.length() > 0) {
                    passports.add(current.toString());
                    current = new StringBuilder();
                }
            } else {
                current.append(" ").append(line);
            }
        }

        if (current.length() > 0) {
            passports.add(current.toString());
        }

        return passports;
    }
}
