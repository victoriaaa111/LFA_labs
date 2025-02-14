package Lab1;

import java.util.*;


public class Grammar
{
    private final Set<String> V_n;
    private final Set<String> V_t;
    private final Map<String, List<String>> P;

    public Grammar(Set<String> V_n, Set<String> V_t, Map<String, List<String>> P) {
        this.V_n = V_n;
        this.V_t = V_t;
        this.P = P;
    }

    public String generateStrings(String startSymbol, int numStrings){
        Set<String> generatedStrings = new HashSet<>();
        while(generatedStrings.size() < numStrings){
            StringBuilder sb = new StringBuilder();
            generateString(startSymbol, sb);
            generatedStrings.add(sb.toString());
        }
        return new ArrayList<>(generatedStrings).toString();
    }

    private void generateString(String symbol, StringBuilder sb) {
        if (!this.P.containsKey(symbol)) {
            sb.append(symbol);
            return;
        }
        List<String> choices = this.P.get(symbol);
        String choice = choices.get(new Random().nextInt(choices.size()));
        for (char c : choice.toCharArray()) {
            if (Character.isUpperCase(c)) {
                generateString(String.valueOf(c), sb);
            } else {
                sb.append(c);
            }
        }
    }

    public FiniteAutomaton toFiniteAutomaton(){
        Set<String> states = V_n;
        Set<String> alphabet= V_t;
        Map<String, Map<String, String>> transitions = new HashMap<>();

        transitions.put("S", Collections.singletonMap("a", "B"));

        transitions.put("B", new HashMap<String, String>(){{
                put("b", "S");
                put("a", "C");
            }});

        transitions.put("C", new HashMap<String, String>(){{
            put("b", "D");
        }});

        transitions.put("D", new HashMap<String, String>(){{
            put("b", "C");
            put("c", "S");
        }});

        FiniteAutomaton finiteAutomaton = new FiniteAutomaton(alphabet, states, "S", new HashSet<>(Arrays.asList("B", "D")), transitions);

        return finiteAutomaton;
    }


}