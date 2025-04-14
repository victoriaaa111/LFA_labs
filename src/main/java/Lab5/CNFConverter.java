package Lab5;

import java.util.*;

class CNFConverter {

    public Grammar convertToCNF(Grammar grammar) {
        Grammar result = grammar;

        System.out.println("\nStep 1: Eliminate ε productions");
        result = eliminateEpsilonProductions(result);
        System.out.println(result);

        System.out.println("\nStep 2: Eliminate renaming");
        result = eliminateRenaming(result);
        System.out.println(result);

        System.out.println("\nStep 3: Eliminate nonproductive symbols");
        result = eliminateNonproductive(result);
        System.out.println(result);

        System.out.println("\nStep 4: Eliminate inaccessible symbols");
        result = eliminateInaccessible(result);
        System.out.println(result);

        System.out.println("\nStep 5: Convert to CNF");
        result = convertToChomsky(result);

        return result;
    }

    // step 1: Eliminate ε productions
    private Grammar eliminateEpsilonProductions(Grammar grammar) {
        String[] rules = grammar.getRules();
        String[] nonTerminals = grammar.getNonTerminals();

        // find all nullable symbols
        Set<String> nullableSymbols = findNullableSymbols(grammar);

        // create new rules with all possible combinations for nullable symbols
        List<String> newRules = generateRulesWithoutEpsilon(grammar, nullableSymbols);

        Grammar result = new Grammar(
                nonTerminals,
                grammar.getTerminals(),
                newRules.toArray(new String[0]),
                grammar.getStartSymbol()
        );

        return result;
    }

    private Set<String> findNullableSymbols(Grammar grammar) {
        String[] rules = grammar.getRules();
        String[] nonTerminals = grammar.getNonTerminals();

        Set<String> nullable = new HashSet<>();
        boolean changed = true;

        for (String rule : rules) {
            String[] parts = rule.split("->");
            String left = parts[0];
            String right = parts[1];

            if (right.equals("ε")) {
                nullable.add(left);
            }
        }

        while (changed) {
            changed = false;

            for (String rule : rules) {
                String[] parts = rule.split("->");
                String left = parts[0];
                String right = parts[1];

                if (!nullable.contains(left)) {
                    boolean allNullable = true;

                    for (int i = 0; i < right.length(); i++) {
                        String symbol = right.substring(i, i + 1);

                        if (!nullable.contains(symbol)) {
                            allNullable = false;
                            break;
                        }
                    }

                    if (allNullable && right.length() > 0) {
                        nullable.add(left);
                        changed = true;
                    }
                }
            }
        }

        return nullable;
    }

    private List<String> generateRulesWithoutEpsilon(Grammar grammar, Set<String> nullableSymbols) {
        String[] rules = grammar.getRules();

        List<String> nonEpsilonRules = new ArrayList<>();

        for (String rule : rules) {
            if (!rule.split("->")[1].equals("ε")) {
                nonEpsilonRules.add(rule);
            }
        }

        Set<String> allRules = new HashSet<>(nonEpsilonRules);

        for (String rule : nonEpsilonRules) {
            String[] parts = rule.split("->");
            String left = parts[0];
            String right = parts[1];

            // Generate all possible combinations of nullable symbols
            for (String nullable : nullableSymbols) {
                if (right.contains(nullable)) {
                    generateCombinations(allRules, left, right, nullable);
                }
            }
        }

        return new ArrayList<>(allRules);
    }

    private void generateCombinations(Set<String> rules, String left, String right, String nullable) {

        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < right.length(); i++) {
            if (i + nullable.length() <= right.length() &&
                    right.substring(i, i + nullable.length()).equals(nullable)) {
                indices.add(i);
            }
        }

        if (!indices.isEmpty()) {
            for (int pos : indices) {
                String newRight = right.substring(0, pos) +
                        right.substring(pos + nullable.length());

                if (!newRight.isEmpty()) {
                    String newRule = left + "->" + newRight;

                    if (rules.add(newRule)) {
                        generateCombinations(rules, left, newRight, nullable);
                    }
                }
            }
        }
    }

    // step 2: Eliminate renaming productions
    private Grammar eliminateRenaming(Grammar grammar) {
        String[] rules = grammar.getRules();
        String[] nonTerminals = grammar.getNonTerminals();

        Set<String> directRenamings = new HashSet<>();

        for (String rule : rules) {
            String[] parts = rule.split("->");
            String left = parts[0];
            String right = parts[1];

            // check if right side is a single nonterminal
            boolean isRenaming = false;
            for (String nt : nonTerminals) {
                if (right.equals(nt)) {
                    isRenaming = true;
                    break;
                }
            }

            if (isRenaming) {
                directRenamings.add(rule);
            }
        }

        Set<String> renamingClosure = new HashSet<>(directRenamings);

        boolean changed = true;
        while (changed) {
            changed = false;
            Set<String> newRenamings = new HashSet<>();

            for (String rule : renamingClosure) {
                String[] parts = rule.split("->");
                String left = parts[0];
                String right = parts[1];

                for (String renaming : directRenamings) {
                    String[] renamingParts = renaming.split("->");
                    String renamingLeft = renamingParts[0];
                    String renamingRight = renamingParts[1];

                    if (right.equals(renamingLeft)) {
                        String newRule = left + "->" + renamingRight;

                        if (!renamingClosure.contains(newRule)) {
                            newRenamings.add(newRule);
                            changed = true;
                        }
                    }
                }
            }

            renamingClosure.addAll(newRenamings);
        }

        Set<String> newRules = new HashSet<>();

        for (String rule : rules) {
            String[] parts = rule.split("->");
            String right = parts[1];

            boolean isRenaming = false;
            for (String nt : nonTerminals) {
                if (right.equals(nt)) {
                    isRenaming = true;
                    break;
                }
            }

            if (!isRenaming) {
                newRules.add(rule);
            }
        }

        for (String renaming : renamingClosure) {
            String[] parts = renaming.split("->");
            String left = parts[0];
            String right = parts[1];

            for (String rule : rules) {
                String[] ruleParts = rule.split("->");
                String ruleLeft = ruleParts[0];
                String ruleRight = ruleParts[1];

                if (right.equals(ruleLeft) && !containsOnlyNonTerminal(ruleRight, nonTerminals)) {
                    newRules.add(left + "->" + ruleRight);
                }
            }
        }

        return new Grammar(
                nonTerminals,
                grammar.getTerminals(),
                newRules.toArray(new String[0]),
                grammar.getStartSymbol()
        );
    }

    private boolean containsOnlyNonTerminal(String right, String[] nonTerminals) {
        for (String nt : nonTerminals) {
            if (right.equals(nt)) {
                return true;
            }
        }
        return false;
    }

    // step 3: Eliminate nonproductive symbols
    private Grammar eliminateNonproductive(Grammar grammar) {
        String[] rules = grammar.getRules();
        String[] nonTerminals = grammar.getNonTerminals();
        String[] terminals = grammar.getTerminals();

        // find all productive symbols
        Set<String> productive = new HashSet<>();

        // first pass: find directly productive symbols (A -> a)
        for (String rule : rules) {
            String[] parts = rule.split("->");
            String left = parts[0];
            String right = parts[1];

            boolean onlyTerminals = true;
            for (int i = 0; i < right.length(); i++) {
                String symbol = right.substring(i, i + 1);
                boolean isTerminal = false;

                for (String term : terminals) {
                    if (symbol.equals(term)) {
                        isTerminal = true;
                        break;
                    }
                }

                if (!isTerminal) {
                    onlyTerminals = false;
                    break;
                }
            }

            if (onlyTerminals) {
                productive.add(left);
            }
        }

        // find indirectly productive symbols
        boolean changed = true;
        while (changed) {
            changed = false;

            for (String rule : rules) {
                String[] parts = rule.split("->");
                String left = parts[0];
                String right = parts[1];

                if (!productive.contains(left)) {
                    boolean allSymbolsProductive = true;

                    for (int i = 0; i < right.length(); i++) {
                        String symbol = right.substring(i, i + 1);

                        boolean isTerminal = false;
                        for (String term : terminals) {
                            if (symbol.equals(term)) {
                                isTerminal = true;
                                break;
                            }
                        }

                        if (!isTerminal && !productive.contains(symbol)) {
                            allSymbolsProductive = false;
                            break;
                        }
                    }

                    if (allSymbolsProductive) {
                        productive.add(left);
                        changed = true;
                    }
                }
            }
        }


        List<String> newRules = new ArrayList<>();

        for (String rule : rules) {
            String[] parts = rule.split("->");
            String left = parts[0];
            String right = parts[1];

            if (productive.contains(left)) {
                boolean allSymbolsProductive = true;

                for (int i = 0; i < right.length(); i++) {
                    String symbol = right.substring(i, i + 1);

                    boolean isTerminal = false;
                    for (String term : terminals) {
                        if (symbol.equals(term)) {
                            isTerminal = true;
                            break;
                        }
                    }

                    if (!isTerminal && !productive.contains(symbol)) {
                        allSymbolsProductive = false;
                        break;
                    }
                }

                if (allSymbolsProductive) {
                    newRules.add(rule);
                }
            }
        }

        // filter productive non-terminals
        List<String> productiveNonTerminals = new ArrayList<>();
        for (String nt : nonTerminals) {
            if (productive.contains(nt)) {
                productiveNonTerminals.add(nt);
            }
        }

        return new Grammar(
                productiveNonTerminals.toArray(new String[0]),
                terminals,
                newRules.toArray(new String[0]),
                grammar.getStartSymbol()
        );
    }

    // step 4: Eliminate inaccessible symbols
    private Grammar eliminateInaccessible(Grammar grammar) {
        String[] rules = grammar.getRules();
        String[] nonTerminals = grammar.getNonTerminals();
        String[] terminals = grammar.getTerminals();
        String startSymbol = grammar.getStartSymbol();

        // find all accessible symbols
        Set<String> accessible = new HashSet<>();
        accessible.add(startSymbol);

        boolean changed = true;
        while (changed) {
            changed = false;

            for (String rule : rules) {
                String[] parts = rule.split("->");
                String left = parts[0];
                String right = parts[1];

                if (accessible.contains(left)) {
                    for (int i = 0; i < right.length(); i++) {
                        String symbol = right.substring(i, i + 1);

                        boolean isNonTerminal = false;
                        for (String nt : nonTerminals) {
                            if (symbol.equals(nt)) {
                                isNonTerminal = true;
                                break;
                            }
                        }

                        if (isNonTerminal && !accessible.contains(symbol)) {
                            accessible.add(symbol);
                            changed = true;
                        }
                    }
                }
            }
        }

        // filter accessible nonterminals
        List<String> accessibleNonTerminals = new ArrayList<>();
        for (String nt : nonTerminals) {
            if (accessible.contains(nt)) {
                accessibleNonTerminals.add(nt);
            }
        }

        // filter accessible terminals
        Set<String> accessibleTerminals = new HashSet<>();
        for (String terminal : terminals) {
            for (String rule : rules) {
                String[] parts = rule.split("->");
                String left = parts[0];
                String right = parts[1];

                if (accessible.contains(left) && right.contains(terminal)) {
                    accessibleTerminals.add(terminal);
                    break;
                }
            }
        }

        // filter rules with accessible symbols
        List<String> accessibleRules = new ArrayList<>();
        for (String rule : rules) {
            String[] parts = rule.split("->");
            String left = parts[0];

            if (accessible.contains(left)) {
                accessibleRules.add(rule);
            }
        }

        return new Grammar(
                accessibleNonTerminals.toArray(new String[0]),
                accessibleTerminals.toArray(new String[0]),
                accessibleRules.toArray(new String[0]),
                startSymbol
        );
    }

    //step 5: Convert to CNF
    public Grammar convertToChomsky(Grammar grammar) {
        String[] rules = grammar.getRules();
        String[] nonTerminals = grammar.getNonTerminals();
        String[] terminals = grammar.getTerminals();
        String startSymbol = grammar.getStartSymbol();

        Set<String> cnfRules = new HashSet<>();
        Map<String, String> terminalToNonTerminal = new HashMap<>();
        List<String> newNonTerminals = new ArrayList<>(Arrays.asList(nonTerminals));
        int newSymbolCounter = 0;

        Map<String, String> patternToY = new HashMap<>();

        // create Xi -> terminal for each terminal
        for (String terminal : terminals) {
            String symbol = "X" + (++newSymbolCounter);
            terminalToNonTerminal.put(terminal, symbol);
            newNonTerminals.add(symbol);
            cnfRules.add(symbol + "->" + terminal);
        }

        for (String rule : rules) {
            String[] split = rule.split("->");
            String left = split[0];
            String right = split[1];

            // check if already in CNF
            if (isInCNF(right, nonTerminals, terminals)) {
                cnfRules.add(rule);
                continue;
            }

            if (right.length() == 1 && Arrays.asList(terminals).contains(right)) {
                // keep original rule for single terminal productions
                cnfRules.add(rule);
                continue;
            }

            // convert right side into list of symbols
            List<String> symbols = new ArrayList<>();
            for (char ch : right.toCharArray()) {
                String sym = String.valueOf(ch);
                // replace terminals with X non-terminals only when they appear in longer productions
                if (Arrays.asList(terminals).contains(sym)) {
                    sym = terminalToNonTerminal.get(sym);
                }
                symbols.add(sym);
            }

            // handle based on number of symbols
            if (symbols.size() == 1) {
                cnfRules.add(left + "->" + symbols.get(0));
            } else if (symbols.size() == 2) {
                cnfRules.add(left + "->" + symbols.get(0) + symbols.get(1));
            } else {
                // for longer productions, apply the Y transformation
                breakDownWithY(left, symbols, cnfRules, patternToY, newNonTerminals);
            }
        }

        return new Grammar(
                newNonTerminals.toArray(new String[0]),
                terminals,
                cnfRules.toArray(new String[0]),
                startSymbol
        );
    }

    // helper method to check if a production is already in CNF
    private boolean isInCNF(String right, String[] nonTerminals, String[] terminals) {
        // case 1: A -> B (single non-terminal)
        if (right.length() == 1) {
            return Arrays.asList(nonTerminals).contains(right);
        }

        // case 2: A -> BC (two non-terminals)
        if (right.length() == 2) {
            String first = right.substring(0, 1);
            String second = right.substring(1, 2);
            return Arrays.asList(nonTerminals).contains(first) &&
                    Arrays.asList(nonTerminals).contains(second);
        }

        return false;
    }

    // break down a long production using Y non-terminals
    private void breakDownWithY(String left, List<String> symbols, Set<String> cnfRules,
                                Map<String, String> patternToY, List<String> newNonTerminals) {

        String firstSymbol = symbols.get(0);

        StringBuilder remainderBuilder = new StringBuilder();
        for (int i = 1; i < symbols.size(); i++) {
            remainderBuilder.append(symbols.get(i));
        }
        String remainder = remainderBuilder.toString();

        String ySymbol;
        if (patternToY.containsKey(remainder)) {
            ySymbol = patternToY.get(remainder);
        } else {
            // create new Y for this pattern
            ySymbol = "Y" + (patternToY.size() + 1);
            patternToY.put(remainder, ySymbol);
            newNonTerminals.add(ySymbol);

            List<String> remainingSymbols = new ArrayList<>();
            for (int i = 1; i < symbols.size(); i++) {
                remainingSymbols.add(symbols.get(i));
            }

            // if remainder has more than 2 symbols, continue breaking down
            if (remainingSymbols.size() > 2) {
                breakDownWithY(ySymbol, remainingSymbols, cnfRules, patternToY, newNonTerminals);
            } else {
                // just two symbols remain, create final Y production
                cnfRules.add(ySymbol + "->" + remainingSymbols.get(0) + remainingSymbols.get(1));
            }
        }

        // create production connecting left to first symbol and Y
        cnfRules.add(left + "->" + firstSymbol + ySymbol);
    }

}