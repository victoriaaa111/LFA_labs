package Lab1;

import java.util.Map;
import java.util.Set;

public class FiniteAutomaton
{
    private final Set<String> alphabet;
    private final String startState;
    private final Set<String> finalStates;
    private final Set<String> states;
    private final Map<String, Map<String, String>> transitions;


    public FiniteAutomaton(Set<String> alphabet, Set<String> states, String startState, Set<String> finalStates, Map<String, Map<String, String>> transitions) {
        this.alphabet = alphabet;
        this.states = states;
        this.startState = startState;
        this.finalStates = finalStates;
        this.transitions = transitions;
    }

    public boolean stringBelongToLanguage(String inputString)
    {
        String currentState = startState;
        for(char symbol : inputString.toCharArray()){
            String symbolString = String.valueOf(symbol);
            if(!alphabet.contains(symbolString)){
                return false;
            }
            if(transitions.containsKey(currentState) && transitions.get(currentState).containsKey(symbolString)){
                currentState = transitions.get(currentState).get(symbolString);
            }else{
                return false;
            }
        }
        return finalStates.contains(currentState);
    }
}
