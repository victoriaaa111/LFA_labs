package Lab2;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Grammar grammar = new Grammar();

        Set<String> nonTerminals = new HashSet<>(Arrays.asList("S", "B", "C", "D"));
        Set<String> terminals = new HashSet<>(Arrays.asList("a", "b", "c"));
        Map<String, Set<String>> productions = new HashMap<>();

        productions.put("S", new HashSet<>(Collections.singletonList("aB")));
        productions.put("B", new HashSet<>(Arrays.asList("bS", "aC", "b")));
        productions.put("C", new HashSet<>(Collections.singletonList("bD")));
        productions.put("D", new HashSet<>(Arrays.asList("a", "bC", "cS")));

        grammar.setV_n(nonTerminals);
        grammar.setV_t(terminals);
        grammar.setP(productions);

        System.out.println("Grammar: ");
        System.out.println(grammar);

        String classification = grammar.classifyGrammar();
        System.out.println("Grammar Classification: " + classification);
    }
}
