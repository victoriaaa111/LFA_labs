package Lab5;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nChoose an option:");
            System.out.println("1. Test with Variant 21 Grammar");
            System.out.println("2. Enter a custom grammar");
            System.out.println("0. Exit");
            System.out.print("Your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    testVariant21Grammar();
                    break;
                case 2:
                    testCustomGrammar(scanner);
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);

        scanner.close();
    }

    public static void testVariant21Grammar() {
        System.out.println("\n=== TESTING VARIANT 21 GRAMMAR ===");

        // Grammar from Variant 21
        String[] rules = {
                "S->dB",
                "S->AC",
                "A->d",
                "A->dS",
                "A->aBdB",
                "B->a",
                "B->aA",
                "B->AC",
                "D->ab",
                "C->bC",
                "C->ε"
        };

        String[] nonTerminals = {"S", "A", "B", "C", "D"};
        String[] terminals = {"a", "b", "d"};

        Grammar grammar = new Grammar(nonTerminals, terminals, rules, "S");

        System.out.println("Original Grammar:");
        System.out.println(grammar);

        CNFConverter converter = new CNFConverter();
        Grammar cnfGrammar = converter.convertToCNF(grammar);

        System.out.println("\nFinal CNF Grammar:");
        System.out.println(cnfGrammar);
    }

    public static void testCustomGrammar(Scanner scanner) {
        System.out.println("\n=== ENTER A CUSTOM GRAMMAR ===");

        // get non terminals
        System.out.print("Enter non-terminal symbols (comma-separated, ex: S,A,B): ");
        String nonTerminalsInput = scanner.nextLine();
        String[] nonTerminals = nonTerminalsInput.split(",");

        // get terminals
        System.out.print("Enter terminal symbols (comma-separated, ex: a,b,c): ");
        String terminalsInput = scanner.nextLine();
        String[] terminals = terminalsInput.split(",");

        // get start symbol
        System.out.print("Enter start symbol: ");
        String startSymbol = scanner.nextLine();

        // get rules
        System.out.print("Enter number of rules: ");
        int numRules = scanner.nextInt();
        scanner.nextLine();

        String[] rules = new String[numRules];
        System.out.println("Enter rules in format 'A->α' (ex: S->aS, A->ε):");

        for (int i = 0; i < numRules; i++) {
            System.out.print("Rule " + (i+1) + ": ");
            rules[i] = scanner.nextLine();
        }

        // create and convert the grammar
        Grammar grammar = new Grammar(nonTerminals, terminals, rules, startSymbol);

        System.out.println("\nOriginal Grammar:");
        System.out.println(grammar);

        CNFConverter converter = new CNFConverter();
        Grammar cnfGrammar = converter.convertToCNF(grammar);

        System.out.println("\nFinal CNF Grammar:");
        System.out.println(cnfGrammar);
    }
}