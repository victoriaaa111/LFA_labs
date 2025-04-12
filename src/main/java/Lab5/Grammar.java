package Lab5;

public class Grammar {
    private String[] nonTerminals;
    private String[] terminals;
    private String[] rules;
    private String startSymbol;

    public Grammar(String[] nonTerminals, String[] terminals, String[] rules, String startSymbol) {
        this.nonTerminals = nonTerminals;
        this.terminals = terminals;
        this.rules = rules;
        this.startSymbol = startSymbol;
    }

    public String[] getNonTerminals() {
        return nonTerminals;
    }

    public void setNonTerminals(String[] nonTerminals) {
        this.nonTerminals = nonTerminals;
    }

    public String[] getTerminals() {
        return terminals;
    }

    public void setTerminals(String[] terminals) {
        this.terminals = terminals;
    }

    public String[] getRules() {
        return rules;
    }

    public void setRules(String[] rules) {
        this.rules = rules;
    }

    public String getStartSymbol() {
        return startSymbol;
    }

    public void setStartSymbol(String startSymbol) {
        this.startSymbol = startSymbol;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("G = (VN, VT, P, S)\n");

        sb.append("VN = {");
        for (int i = 0; i < nonTerminals.length; i++) {
            sb.append(nonTerminals[i]);
            if (i < nonTerminals.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("}\n");

        sb.append("VT = {");
        for (int i = 0; i < terminals.length; i++) {
            sb.append(terminals[i]);
            if (i < terminals.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("}\n");

        sb.append("S = ").append(startSymbol);

        sb.append("P = {\n");
        for (int i = 0; i < rules.length; i++) {
            sb.append("    ").append(i + 1).append(". ").append(rules[i]).append("\n");
        }
        sb.append("}\n");



        return sb.toString();
    }
}