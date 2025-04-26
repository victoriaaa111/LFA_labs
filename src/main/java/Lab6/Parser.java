package Lab6;
import Lab3.Token;
import Lab3.TokenType;
import java.util.List;


public class Parser {
    private List<Token> tokens;
    private int position;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.position = 0;
    }

    private Token getCurrentToken() {
        return position < tokens.size() ? tokens.get(position) : new Token(TokenType.EOL, "");
    }

    private void advance() {
        position++;
    }

    public ASTNode parse() {
        return parseExpression();
    }

    private ASTNode parseExpression() {
        ASTNode node = parseTerm();

        while (getCurrentToken().getType() == TokenType.PLUS ||
                getCurrentToken().getType() == TokenType.MINUS) {
            Token op = getCurrentToken();
            advance();
            ASTNode right = parseTerm();
            ASTNode opNode = new ASTNode(NodeType.OPERATION, op.getType().toString());
            opNode.addChild(node);
            opNode.addChild(right);
            node = opNode;
        }

        return node;
    }

    private ASTNode parseTerm() {
        ASTNode node = parseFactor();

        while (getCurrentToken().getType() == TokenType.MULTIPLY ||
                getCurrentToken().getType() == TokenType.DIVIDE) {
            Token op = getCurrentToken();
            advance();
            ASTNode right = parseFactor();
            ASTNode opNode = new ASTNode(NodeType.OPERATION, op.getType().toString());
            opNode.addChild(node);
            opNode.addChild(right);
            node = opNode;
        }

        return node;
    }

    private ASTNode parseFactor() {
        Token token = getCurrentToken();

        if (token.getType() == TokenType.NUMBER) {
            advance();
            return new ASTNode(NodeType.NUMBER, token.getValue());
        } else if (token.getType() == TokenType.SIN || token.getType() == TokenType.COS || token.getType() == TokenType.TAN) {
            advance();
            expect(TokenType.LPAREN);
            ASTNode argument = parseExpression();
            expect(TokenType.RPAREN);
            ASTNode funcNode = new ASTNode(NodeType.FUNCTION, token.getType().toString());
            funcNode.addChild(argument);
            return funcNode;
        } else if (token.getType() == TokenType.LPAREN) {
            advance();
            ASTNode node = parseExpression();
            expect(TokenType.RPAREN);
            return node;
        } else {
            throw new RuntimeException("Unexpected token: " + token);
        }
    }

    private void expect(TokenType type) {
        if (getCurrentToken().getType() == type) {
            advance();
        } else {
            throw new RuntimeException("Expected " + type + " but got " + getCurrentToken().getType());
        }
    }
}
