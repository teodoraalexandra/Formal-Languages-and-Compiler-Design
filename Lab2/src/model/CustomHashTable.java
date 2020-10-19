package model;

import java.util.Arrays;

public class CustomHashTable {

    private String[] symTable;
    private int capacity;

    public CustomHashTable(int capacity) {
        this.capacity = capacity;
        this.symTable = new String[capacity];
    }

    private int hashFunction(String identifier) {
        int sum = 0;
        for(int i = 0; i < identifier.length(); i++) {
            sum += identifier.charAt(i);
        }
        return sum % this.capacity;
    }

    /*
     * Add the identifier to the symbol table
     * We solve collision using Linear Probing
     *
     * params: String identifier
     * return: TRUE if the identifier doesn't exist in symbol table
     *       FALSE otherwise
     *
     * */

    public boolean insert(String identifier) {
        // Check if already in sym table
        for (String s : symTable) {
            if (s != null && s.equals(identifier)) {
                System.out.println("Already in sym table.");
                return false;
            }
        }
        int hashValue = hashFunction(identifier);
        if (symTable[hashValue] == null) {
            symTable[hashValue] = identifier;
            System.out.println("Insert " + identifier + " at position " + hashValue);
            return true;
        }
        // Else, we have a collision
        int nextAvailablePosition = hashValue;
        while (symTable[nextAvailablePosition] != null) {
            nextAvailablePosition++;
        }
        if (symTable[nextAvailablePosition] == null) {
            symTable[nextAvailablePosition] = identifier;
            System.out.println("Insert " + identifier + " at position " + nextAvailablePosition);
            return true;
        }
        System.out.println("Insert failed.");
        return false;
    }

    /*
     * Search for the identifier in the symbol table
     *
     * params: String identifier
     * return: position (INTEGER) if the identifier exists in symbol table
     *       -1 otherwise
     *
     * */

    public int find(String identifier) {
        int hashValue = hashFunction(identifier);
        while (symTable[hashValue] != null) {
            if(symTable[hashValue].equalsIgnoreCase(identifier)) {
                return hashValue;
            }
            hashValue ++;
        }
        return -1;
    }

    @Override
    public String toString() {
        return "HashTable{" +
                "symTable=" + Arrays.toString(symTable) +
                '}';
    }
}
