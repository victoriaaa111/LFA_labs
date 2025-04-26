package Lab6;
import Lab3.Token;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter operation: ");
        String input = scanner.nextLine();

        Lexer lexer = new Lexer(input);
        List<Token> tokens = lexer.tokenize();

        Parser parser = new Parser(tokens);

        try {
            ASTNode ast = parser.parse();
            ast.print("", true);
        } catch (RuntimeException e) {
            System.out.println("Invalid expression");
        }
    }
}
