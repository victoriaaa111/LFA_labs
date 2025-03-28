package Lab4;
import java.util.Random;

public class RegexParser {
    private static final Random RANDOM = new Random();
    private String regex;
    private int index;
    private int repetitionLimit;

    public RegexParser(String regex, int repetitionLimit) {
        this.regex = regex;
        this.index = 0;
        this.repetitionLimit = repetitionLimit;
    }

    public void parse(StringBuilder result) {
        while (index < regex.length()) {
            char current = regex.charAt(index);
            if (current == '(') {
                result.append(handleGroup());
            } else if (current == '[') {
                result.append(handleSet());
            } else if (Character.isLetterOrDigit(current)) {
                result.append(current);
            } else if (current == '*' || current == '+' || current == '?') {
                handleQuantifier(result, current);
            }
            index++;
        }
    }

    private String handleGroup() {
        index++;
        StringBuilder group = new StringBuilder();
        while (index < regex.length() && regex.charAt(index) != ')') {
            group.append(regex.charAt(index++));
        }
        String[] options = group.toString().split("\\|");
        return options[RANDOM.nextInt(options.length)];
    }

    private String handleSet() {
        index++;
        StringBuilder set = new StringBuilder();
        while (index < regex.length() && regex.charAt(index) != ']') {
            set.append(regex.charAt(index++));
        }
        return String.valueOf(set.charAt(RANDOM.nextInt(set.length())));
    }

    private void handleQuantifier(StringBuilder result, char quantifier) {
        int repeat = (quantifier == '?') ? RANDOM.nextInt(2) : RANDOM.nextInt(repetitionLimit) + (quantifier == '*' ? 0 : 1);
        char lastChar = result.charAt(result.length() - 1);
        for (int i = 1; i < repeat; i++) {
            result.append(lastChar);
        }
    }
}
