import model.CustomHashTable;

public class Main {

    public static void main(String[] args) {
        System.out.println("Inserting values ...");
        CustomHashTable symTable = new CustomHashTable(17);

        // Inserting some values
        symTable.insert("a");
        symTable.insert("ab");
        symTable.insert("cd");
        symTable.insert("dc");
        symTable.insert("ba");
        symTable.insert("a");

        System.out.println("\nStarting tests ...");
        // Test if all values have been inserted (all should return true)
        assert symTable.insert("a");
        assert symTable.insert("ab");
        assert symTable.insert("cd");
        assert symTable.insert("dc");
        assert symTable.insert("ba");
        assert !symTable.insert("a");

        assert 12 == symTable.find("a");
        assert 8 == symTable.find("ab");
        assert -1 == symTable.find("teodora");
        System.out.println(symTable);
    }
}
