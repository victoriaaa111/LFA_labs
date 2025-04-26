package Lab6;

import java.util.ArrayList;
import java.util.List;

public class ASTNode {
    private NodeType type;
    private String value;
    private List<ASTNode> children;

    public ASTNode(NodeType type, String value) {
        this.type = type;
        this.value = value;
        this.children = new ArrayList<>();
    }

    public void addChild(ASTNode child) {
        children.add(child);
    }

    public void print(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + type.name() + "(" + value + ")");
        for (int i = 0; i < children.size(); i++) {
            children.get(i).print(prefix + (isTail ? "    " : "│   "), i == children.size() - 1);
        }
    }
}
