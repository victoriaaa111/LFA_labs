package Lab2;

import java.util.*;

public class Grammar {
    private Set<String> V_n;
    private Set<String> V_t;
    private Map<String, Set<String>> P;

    public Grammar() {
        this.V_n = new HashSet<>();
        this.V_t = new HashSet<>();
        this.P = new HashMap<>();
    }

    public String classifyGrammar(){
        boolean isRegular = true;
        boolean isContextFree = true;
        boolean isContextSensitive = true;

        for(String nonTerminal: this.P.keySet()){
            for(String production: this.P.get(nonTerminal)){
                if(!((production.matches("[a-z]([A-Z])?") || production.matches("([A-Z])?[a-z]")))){
                    isRegular = false;
                }
                if (nonTerminal.length() != 1 || !this.V_n.contains(nonTerminal)) {
                    isContextFree = false;
                }
                if(!production.equals("ε") && production.length() < nonTerminal.length()){
                    isContextSensitive = false;
                }
            }
        }
        if(isRegular){
            return "Regular Grammar";
        }else if(isContextFree){
            return "Context-Free Grammar";
        }else if(isContextSensitive){
            return "Context-Sensitive Grammar";
        }else{
            return "Unrestricted Grammar";
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Non-terminals: ").append(this.V_n.isEmpty() ? "{}" : this.V_n).append("\n");
        sb.append("Terminals: ").append(this.V_t.isEmpty() ? "{}" : this.V_t).append("\n");
        sb.append("Productions: \n");
        for(Map.Entry<String, Set<String>> entry : this.P.entrySet()){
            String nonTerminal = entry.getKey();
            sb.append(nonTerminal).append(" : ");
            Set<String> productions = entry.getValue();
            if(!productions.isEmpty()){
                Iterator<String> iterator = productions.iterator();
                sb.append(iterator.next());
                while(iterator.hasNext()){
                    sb.append(" | ").append(iterator.next());
                }
            }else{
                sb.append("{}");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    //getters and setters
    public Set<String> getV_n() {
        return V_n;
    }

    public Set<String> getV_t() {
        return V_t;
    }

    public Map<String, Set<String>> getP() {
        return P;
    }

    public void setV_n(Set<String> v_n) {
        V_n = v_n;
    }

    public void setV_t(Set<String> v_t) {
        V_t = v_t;
    }

    public void setP(Map<String, Set<String>> p) {
        P = p;
    }
}
