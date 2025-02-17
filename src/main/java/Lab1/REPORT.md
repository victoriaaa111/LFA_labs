# LAB 1 : Regular Grammars

### Course: Formal Languages & Finite Automata
### Author: Victoria Mutruc

----

## Theory

A formal language consists of several essential components: an alphabet, a vocabulary, and a grammar. The alphabet is a 
set of valid characters, the vocabulary includes the valid words or strings formed from these characters, and the 
grammar defines the rules for how words can be structured within the language.
Formal languages are crucial in fields such as computer science, linguistics, and mathematics, as they provide a 
precise, unambiguous means of communication.

A grammar dictates how strings within the language are constructed. It includes non-terminal symbols, which can be 
replaced by other symbols or strings, and terminal symbols, which form the final structure of the string. 
Production rules define how strings can be derived by transforming one symbol into another. This structure is 
fundamental for determining the validity of strings within the language.

Finite Automata (FA) are mathematical models used to represent formal languages. A Finite Automaton is composed of a set
of states, input symbols, a transition function that describes the state changes based on input, a start state, and a 
set of final states. The FA operates by processing input strings and determining whether they belong to the language by 
checking if the final state is reached.

## Objectives:

1. Discover what a language is and what it needs to have in order to be considered a formal one;
2. Provide the initial setup for the evolving project that you will work on during this semester. You can deal with each laboratory work as a separate task or project to demonstrate your understanding of the given themes, but you also can deal with labs as stages of making your own big solution, your own project. Do the following:

    a. Create GitHub repository to deal with storing and updating your project;
    
    b. Choose a programming language. Pick one that will be easiest for dealing with your tasks, you need to learn how to solve the problem itself, not everything around the problem (like setting up the project, launching it correctly and etc.);
    
    c. Store reports separately in a way to make verification of your work simpler 

3. According to your variant number, get the grammar definition and do the following:

    a. Implement a type/class for your grammar;

    b. Add one function that would generate 5 valid strings from the language expressed by your given grammar;
    
    c. Implement some functionality that would convert and object of type Grammar to one of type Finite Automaton;
    
    d. For the Finite Automaton, please add a method that checks if an input string can be obtained via the state transition from it;
4. Implement the laboratory work for the following variant:
   ```
   Variant 21:
   VN={S, B, C, D},
   VT={a, b, c},
   P={
   S → aB     
   B → bS    
   B → aC   
   B → b    
   C → bD   
   D → a    
   D → bC
   D → cS
   }


## Implementation description

### The stringBelongToLanguage method

* At the beginning of the method, currentStates is initialized as a set containing only the starting state. Since this automaton is nondeterministic, multiple states can be active at once, but initially, only the start state is considered.

   ```
   Set<String> currentStates = new HashSet<>();
   currentStates.add(startState);
   
   ```

* Before processing the character, the method verifies whether it exists in the automaton’s alphabet. If the symbol is not part of the alphabet, an error is displayed, and the input is immediately rejected.
   ```
   if (!alphabet.contains(symbolString)) {
    System.out.println("ERROR: Symbol '" + symbolString + "' not in alphabet!");
    return false;
   }
  
   ```
* A new set, nextStates, is created to store the possible states the automaton can transition to after reading the symbol. The method loops through all current active states, then it retrieves the transition rules for each state. If a transition exists for the current input symbol, the corresponding next states are added to nextStates. Since this is an NFA, multiple transitions are possible, meaning multiple next states can exist.
   ```
   Set<String> nextStates = new HashSet<>();
   for (String state : currentStates) {
   Map<String, Set<String>> stateTransitions = transitions.get(state);
   if (stateTransitions != null && stateTransitions.containsKey(symbolString)) {
   nextStates.addAll(stateTransitions.get(symbolString));
   }
   }
  
   ```
*  Once all symbols have been processed, the method checks if any of the active states belong to the set of final states. If at least one final state is reached, the input string is accepted. Otherwise, it is rejected.
   ```
     for (String state : currentStates) {
       if (finalStates.contains(state)) {
           return true;
       }
   }
   return false;

   ```

### The generateStrings method
* The generateStrings method generates a specified number of distinct strings based on a given start symbol and the production rules. First, it initializes a Set<String> called generatedStrings to store the unique strings. The method then enters a while loop that continues until the set contains the desired number of distinct strings (numStrings). Inside the loop, a StringBuilder (sb) is created to build a new string, and the generateString method is called to generate a single string based on the startSymbol, appending it to sb. After the string is generated, it's converted to a String and added to the generatedStrings set. The loop ensures that only unique strings are stored, as the Set automatically handles duplicates. Once the set contains the specified number of strings, the method returns the results as a List<String> by converting the Set into an ArrayList.

   ```
   public List<String> generateStrings(String startSymbol, int numStrings){
           Set<String> generatedStrings = new HashSet<>();
           while(generatedStrings.size() < numStrings){
               StringBuilder sb = new StringBuilder();
               generateString(startSymbol, sb);
               generatedStrings.add(sb.toString());
           }
           return new ArrayList<>(generatedStrings);
       }
   
   ```

### The generateString method

* The generateString method recursively processes a symbol, and if it's a terminal, it appends it directly to the output
string. If the symbol is non-terminal, it looks up the corresponding production rules and randomly selects one to 
further expand the string. This process continues until the entire string is formed by terminal symbols, representing a 
valid word in the language defined by the grammar.
   ```
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
   ```

### The toFiniteAutomaton method
* The automaton's states are initialized using the set of non-terminal symbols (V_n), while the alphabet consists of terminal symbols (V_t). A nested map structure is used to represent state transitions. The outer map associates a state with its corresponding transitions, while the inner map links an input symbol to a set of possible next states. This structure allows the automaton to support nondeterministic transitions.

   ```
   Set<String> states = new HashSet<>(V_n);
   Set<String> alphabet = new HashSet<>(V_t);
   Map<String, Map<String, Set<String>>> transitions = new HashMap<>();
   ```

* The transitions for all states are defined using a nested mapping structure. Each state is mapped to another map that associates input symbols with possible next states. The transitions are as follows: "S" moves to "B" on input 'a', "B" moves to "S" or "B" on 'b', and to "C" on 'a', "C" moves to "D" on 'b', "D" loops on 'a', moves to "C" on 'b', and moves to "S" on 'c'. This structure ensures that the automaton follows its defined rules and supports nondeterministic behavior where multiple transitions are possible.

   ```
   transitions.put("S", new HashMap<>() {{
            put("a", new HashSet<>(Collections.singletonList("B")));
        }});

        transitions.put("B", new HashMap<>() {{
            put("b", new HashSet<>(Arrays.asList("S", "B")));  // multiple transitions for "b"
            put("a", new HashSet<>(Collections.singletonList("C")));
        }});

        transitions.put("C", new HashMap<>() {{
            put("b", new HashSet<>(Collections.singletonList("D")));
        }});

        transitions.put("D", new HashMap<>() {{
            put("a", new HashSet<>(Collections.singletonList("D")));
            put("b", new HashSet<>(Collections.singletonList("C")));
            put("c", new HashSet<>(Collections.singletonList("S")));
        }});
   ```

* The final states determine whether the input is accepted by the automaton. In this case, "B" and "D" are set as final states, meaning that if the automaton ends in either of these states after processing an input string, the string is accepted. This allows the automaton to recognize specific patterns in input sequences.
   ```
   Set<String> finalStates = new HashSet<>();
        finalStates.add("B");
        finalStates.add("D");
   ```

## Results
In the output we are first presented with 5 generated strings that belong to the proposed language. Then the program prints that the word "ab" is accepted by the automaton, as this is the default word I set in the code.
We are then asked if we would like to check another word. If we enter "yes", we will be prompted to enter another word and the program will check if it is accepted by the automaton. In the following screenshot we entered the word "abab" and it is part of the language, since S-> aB -> abaB -> abab.

<img src="results.png" alt="Results" width="400"/>
    

## Conclusions
Through this lab, I learned how to define and implement a regular grammar, generate valid strings from it, and convert 
it into a finite automaton. One of the main challenges I faced was handling transitions in the automaton using a Map, as I initially encountered issues with duplicate keys when multiple transitions were needed for the same state and input symbol. Overcoming this required restructuring my transition representation to allow multiple possible next states, which improved my understanding of how finite automata manage state transitions.

## References
1. GeeksforGeeks. *Map Interface in Java with Examples*. Available at: [https://www.geeksforgeeks.org/map-interface-java-examples/](https://www.geeksforgeeks.org/map-interface-java-examples/)
2. GeeksforGeeks. *Set in Java*. Available at: [https://www.geeksforgeeks.org/set-in-java/](https://www.geeksforgeeks.org/set-in-java/)