package model;

import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Grammar {
    private List<String> setOfNonTerminals;
    private List<String> setOfTerminals;
    private HashMap<String, List<String>> setOfProductions;
    private String startingSymbol;
    private String fileName;

    public Grammar(String fileName) {
        this.fileName = fileName;
        this.setOfNonTerminals = new ArrayList<String>();
        this.setOfTerminals = new ArrayList<String>();
        this.setOfProductions = new HashMap<>();
        this.startingSymbol = "";
    }

    public void readFromFile() throws FileNotFoundException {
        File file = new File(this.fileName);
        Scanner scanner = new Scanner(file);

        // Set of non-terminals
        String setOfNonTerminalsText = scanner.nextLine();
        String setOfNonTerminals = scanner.nextLine();
        this.setOfNonTerminals = Arrays.asList(setOfNonTerminals.split(","));

        // Set of terminals
        String setOfTerminalsText = scanner.nextLine();
        String setOfTerminals = scanner.nextLine();
        this.setOfTerminals = Arrays.asList(setOfTerminals.split(","));

        // Productions
        String productionsText = scanner.nextLine();
        String production = "";

        //  As long as we have productions, we should read them
        while (true) {
            production = scanner.nextLine();
            if (production.equals("STARTING SYMBOL")) {
                break;
            }

            List<String> productions = Arrays.asList(production.split(" -> "));
            List<String> states = Arrays.asList(productions.get(1).split(" \\| "));

            Pair<String, List<String>> model = new Pair<>(productions.get(0), states);

            this.setOfProductions.put(model.getKey(), model.getValue());
        }

        // Starting symbol
        this.startingSymbol = scanner.nextLine();

        scanner.close();
    }

    public List<String> getSetOfNonTerminals() {
        return setOfNonTerminals;
    }

    public void setSetOfNonTerminals(List<String> setOfNonTerminals) {
        this.setOfNonTerminals = setOfNonTerminals;
    }

    public List<String> getSetOfTerminals() {
        return setOfTerminals;
    }

    public void setSetOfTerminals(List<String> setOfTerminals) {
        this.setOfTerminals = setOfTerminals;
    }

    public HashMap<String, List<String>> getSetOfProductions() {
        return setOfProductions;
    }

    public void setSetOfProductions(HashMap<String, List<String>> setOfProductions) {
        this.setOfProductions = setOfProductions;
    }

    public String getStartingSymbol() {
        return startingSymbol;
    }

    public void setStartingSymbol(String startingSymbol) {
        this.startingSymbol = startingSymbol;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<String> productionForNonTerminal(String nonTerminal) {
        return this.setOfProductions.get(nonTerminal);
    }
}
