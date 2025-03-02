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

        FiniteAutomaton fa = new FiniteAutomaton();

        Set<Character> alphabet = new HashSet<>();
        alphabet.add('a');
        alphabet.add('b');
        alphabet.add('c');
        fa.setAlphabet(alphabet);

        fa.addState("q0");
        fa.addState("q1");
        fa.addState("q2");
        fa.addState("q3");
        fa.setStartState("q0");
        fa.addFinalState("q3");
        fa.addTransition("q0", 'a', "q0");
        fa.addTransition("q0", 'a', "q1");
        fa.addTransition("q1", 'b', "q2");
        fa.addTransition("q2", 'c', "q3");
        fa.addTransition("q3", 'c', "q3");
        fa.addTransition("q2", 'a', "q2");

        //convert fa to regular grammar
        Grammar regularGrammar = fa.convertToRegularGrammar();
        System.out.println("Regular Grammar:");
        System.out.println(regularGrammar);


        String result = regularGrammar.classifyGrammar();
        System.out.println("Converted to Regular Grammar: " + ((result.equals("Type 3: Regular Grammar"))? "correct" : "incorrect"));

        System.out.println("Finite Automaton is " + (fa.isDeterministic() ? "deterministic" : "non-deterministic"));

        // convert NFA to DFA
        if(!fa.isDeterministic()) {
            FiniteAutomaton dfa = fa.convertToDFA();
            System.out.println("Converted NFA to DFA:");
            System.out.println(dfa);
        }

    }
}
