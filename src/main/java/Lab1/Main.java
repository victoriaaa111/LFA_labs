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
        System.out.println("Generated strings: ");
        for (String string : generatedStrings) {
            System.out.println(string);
        }
        String check = "ab";
        System.out.println("Word " + check + " accepted by automaton: " + finiteAutomaton.stringBelongToLanguage(check));

        Scanner scanner = new Scanner(System.in);
        System.out.println("Would you like to check another word?");

        if(scanner.nextLine().equals("yes")) {
            System.out.println("What is the word you would like to check?");
            check = scanner.nextLine();
            System.out.println("Word " + check + " accepted by automaton: " + finiteAutomaton.stringBelongToLanguage(check));
        }else{
            scanner.close();
        }
        scanner.close();


    }
}