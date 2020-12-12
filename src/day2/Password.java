package day2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Password {
    private char character;
    private int num1;
    private int num2;
    private String password;

    public Password(String line) {
        Pattern pattern = Pattern.compile("^(\\d+)-(\\d+) ([a-z]): (\\w+)$");
        Matcher matcher = pattern.matcher(line);

        if (matcher.matches()) {
            this.num1 = Integer.parseInt(matcher.group(1));
            this.num2 = Integer.parseInt(matcher.group(2));
            this.character = matcher.group(3).charAt(0);
            this.password = matcher.group(4);
        }
    }

    public boolean isValidPart1() {
        long count = password.chars().filter(c -> c == character).count();
        return count >= num1 && count <= num2;
    }

    public boolean isValidPart2() {
        boolean position1Matches = password.charAt(num1 - 1) == character;
        boolean position2Matches = password.charAt(num2 - 1) == character;
        return position1Matches != position2Matches;
    }
}
