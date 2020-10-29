import model.CustomHashTable;
import model.CustomScanner;

public class Main {

    public static void main(String[] args) {
        CustomScanner scanner = new CustomScanner("programs/p1err");
        scanner.scan();
        scanner.classifyTokens();
    }
}
