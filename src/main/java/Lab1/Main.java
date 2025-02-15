package Lab1;

import java.util.*;

public class Main {
    public static void main(String[] args) {
    Set<String> nonTerminals = new HashSet<>(Arrays.asList("S", "B", "C", "D"));
    Set<String> terminals = new HashSet<>(Arrays.asList("a", "b", "c"));

    Map<String, List<String>> productions = new HashMap<>();
    productions.put("S", Collections.singletonList("aB"));
    productions.put("B", Arrays.asList("bS", "aC", "b"));
    productions.put("C", Collections.singletonList("bD"));
    productions.put("D", Arrays.asList("a", "bC", "cS"));

    Grammar grammar = new Grammar(nonTerminals, terminals, productions);

    List<String> generatedStrings = grammar.generateStrings("S", 5);
    FiniteAutomaton finiteAutomaton = grammar.toFiniteAutomaton();
    for(String string : generatedStrings) {
        System.out.println(string);
    }
    String check = "abab";
    System.out.println(finiteAutomaton.stringBelongToLanguage(check));

    }
}