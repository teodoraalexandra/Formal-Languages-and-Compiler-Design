import model.CustomHashTable;
import model.CustomScanner;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        CustomScanner scanner = new CustomScanner("programs/p1err");
        scanner.scan();

        // PIF
        scanner.classifyTokens();

        // ST
        scanner.writeToSymbolTable();
    }
}
