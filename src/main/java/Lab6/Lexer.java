package Lab6;

import Lab3.Token;
import Lab3.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private final String input;
    private static final Pattern tokenPatterns;

    static {
        StringBuilder patternsBuilder = new StringBuilder();
        patternsBuilder.append("|(?<NUMBER>\\d+(\\.\\d+)?)");
        patternsBuilder.append("|(?<SIN>sin)");
        patternsBuilder.append("|(?<COS>cos)");
        patternsBuilder.append("|(?<TAN>tan)");
        patternsBuilder.append("|(?<PLUS>\\+)");
        patternsBuilder.append("|(?<MINUS>\\-)");
        patternsBuilder.append("|(?<MULTIPLY>\\*)");
        patternsBuilder.append("|(?<DIVIDE>/)");
        patternsBuilder.append("|(?<POWER>\\^)");
        patternsBuilder.append("|(?<LPAREN>\\()");
        patternsBuilder.append("|(?<RPAREN>\\))");
        patternsBuilder.append("|(?<WHITESPACE>\\s+)");
        tokenPatterns = Pattern.compile(patternsBuilder.substring(1));
    }

    public Lexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = tokenPatterns.matcher(input);

        while (matcher.find()) {
            if (matcher.group("NUMBER") != null) {
                tokens.add(new Token(TokenType.NUMBER, matcher.group("NUMBER")));
            } else if (matcher.group("SIN") != null) {
                tokens.add(new Token(TokenType.SIN, matcher.group("SIN")));
            } else if (matcher.group("COS") != null) {
                tokens.add(new Token(TokenType.COS, matcher.group("COS")));
            } else if (matcher.group("TAN") != null) {
                tokens.add(new Token(TokenType.TAN, matcher.group("TAN")));
            } else if (matcher.group("PLUS") != null) {
                tokens.add(new Token(TokenType.PLUS, matcher.group("PLUS")));
            } else if (matcher.group("MINUS") != null) {
                tokens.add(new Token(TokenType.MINUS, matcher.group("MINUS")));
            } else if (matcher.group("MULTIPLY") != null) {
                tokens.add(new Token(TokenType.MULTIPLY, matcher.group("MULTIPLY")));
            } else if (matcher.group("DIVIDE") != null) {
                tokens.add(new Token(TokenType.DIVIDE, matcher.group("DIVIDE")));
            } else if (matcher.group("POWER") != null) {
                tokens.add(new Token(TokenType.POWER, matcher.group("POWER")));
            } else if (matcher.group("LPAREN") != null) {
                tokens.add(new Token(TokenType.LPAREN, matcher.group("LPAREN")));
            } else if (matcher.group("RPAREN") != null) {
                tokens.add(new Token(TokenType.RPAREN, matcher.group("RPAREN")));
            } else if (matcher.group("WHITESPACE") != null) {
                // skip whitespaces
            }
        }

        tokens.add(new Token(TokenType.EOL, "")); // End Of Line token
        return tokens;
    }
}
