import model.FiniteAutomata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {

    public static void main(String[] args) throws IOException {
        FiniteAutomata FA = new FiniteAutomata("/Users/teodoradan/Desktop/Formal-Languages-and-Compiler-Design/Lab4/src/fa.txt");
        FA.readFromFile();

        while (true) {
            display_menu();
            String command = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter command: ");
            command = reader.readLine();

            switch (command) {
                case "1":
                    System.out.println("States: ");
                    System.out.println(FA.getSetOfStates());
                    System.out.println("\n");
                    break;
                case "2":
                    System.out.println("Alphabet: ");
                    System.out.println(FA.getAlphabet());
                    System.out.println("\n");
                    break;
                case "3":
                    System.out.println("Transitions: ");
                    System.out.println(FA.getTransitionsList());
                    System.out.println("\n");
                    break;
                case "4":
                    System.out.println("Final states: ");
                    System.out.println(FA.getFinalStates());
                    System.out.println("\n");
                    break;
                case "5":
                    String message = FA.isDFA() ? "is a DFA" : "is NOT a DFA";
                    System.out.println("The introduced FA " + message);
                    System.out.println("\n");
                    break;
                case "6":
                    if(!FA.isDFA()){
                        System.out.println("FA needs to be a DFA.");
                        break;
                    }
                    BufferedReader reader2 =  new BufferedReader(new InputStreamReader(System.in));
                    System.out.println("Enter sequence: ");
                    String sequence = reader2.readLine();
                    String message2 = FA.isAccepted(sequence) ? "Sequence is accepted" : "Sequence is not accepted";
                    System.out.println(message2);
                    System.out.println("\n");
                    break;
                case "0":
                    System.exit(0);
                default:
                    System.err.println("Unrecognized option");
                    break;
            }
        }
    }

    private static void display_menu() {
        System.out.println("1 - Show states");
        System.out.println("2 - Show alphabet");
        System.out.println("3 - Show transitions");
        System.out.println("4 - Show final states");
        System.out.println("5 - Is DFA?");
        System.out.println("6 - Verify if a sequence is accepted by FA");
        System.out.println("0 - Exit \n");
    }
}
