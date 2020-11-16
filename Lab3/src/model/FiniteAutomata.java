package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FiniteAutomata {
    private List<String> setOfStates;
    private List<String> alphabet;
    private List<Transition> transitionsList;
    private List<String> finalStates;
    private String initialState;
    private String fileName;

    public FiniteAutomata(String fileName) {
        this.fileName = fileName;
        this.setOfStates = new ArrayList<String>();
        this.alphabet = new ArrayList<String>();
        this.transitionsList = new ArrayList<Transition>();
        this.finalStates = new ArrayList<String>();
        this.initialState = "";
    }

    public void readFromFile() throws FileNotFoundException {
        File file = new File(this.fileName);
        Scanner scanner = new Scanner(file);

        // Set of states
        String setOfStatesText = scanner.nextLine();
        String setOfStates = scanner.nextLine();
        this.setOfStates = Arrays.asList(setOfStates.split(","));

        // Alphabet
        String alphabetText = scanner.nextLine();
        String alphabet = scanner.nextLine();
        this.alphabet = Arrays.asList(alphabet.split(","));

        // Transitions
        String transitionsText = scanner.nextLine();
        String transition = "";

        //  As long as we have transitions, we should read them
        while (true) {
            transition = scanner.nextLine();
            if (transition.equals("FINAL STATES")) {
                break;
            }

            List<String> transitions = Arrays.asList(transition.split(","));
            Transition model = new Transition();
            model.setStartState(transitions.get(0));
            model.setValue(transitions.get(1));
            List<String> endStates = new ArrayList<String>();
            for (int i = 2; i < transitions.size(); i++) {
                endStates.add(transitions.get(i));
            }
            model.setEndState(endStates);

            this.transitionsList.add(model);
        }

        // Final states
        String finalStates = scanner.nextLine();
        this.finalStates = Arrays.asList(finalStates.split(","));

        // Initial state
        String initialState = scanner.nextLine();
        this.initialState = scanner.nextLine();

        scanner.close();
    }

    public List<String> getSetOfStates() {
        return setOfStates;
    }

    public List<String> getAlphabet() {
        return alphabet;
    }

    public List<Transition> getTransitionsList() {
        return transitionsList;
    }

    public List<String> getFinalStates() {
        return finalStates;
    }

    public String getInitialState() { return initialState; }

    public boolean isDFA() {
        for (Transition transition: transitionsList) {
            if (transition.getEndState().size() > 1) {
                return false;
            }
        }
        return true;
    }

    public boolean isAccepted(String seq) {
        String currentState = this.initialState;
        String[] sequence = seq.split("");
        for (String character : sequence) {
            String nextState = nextState(currentState, character);

            // Case: no state
            if (nextState.equals("no-state-found")) return false;

            currentState = nextState;
        }
        // Case: final state
        return this.finalStates.contains(currentState);
    }

    private String nextState(String startState, String value) {
        for (Transition transition: transitionsList) {
            if (transition.getStartState().equals(startState) && transition.getValue().equals(value))
                if (transition.getEndState().size() == 1)
                    return transition.getEndState().get(0);
        }
        return "no-state-found";
    }
}
