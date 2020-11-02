package model;

import javafx.util.Pair;

import java.io.*;
import java.util.*;

/**
 * While (not(eof)) do
 *     detect(token);
 *     if token is reserved word OR operator OR separator
 *         then genPIF(token, 0)
 *         else
 *         if token is identifier OR constant
 *             then index = pos(token, ST);
 *                 genPIF(token, index)
 *             else message “Lexical error”
 *         endif
 *     endif
 * end while
 **/

public class CustomScanner {
    private String fileName;
    private boolean isStringLexicallyCorrect = true;
    private boolean isCharLexicallyCorrect = true;
    private List<String> tokenList = new ArrayList<String>();
    private List<String> separatorList = new ArrayList<String>();
    private List<Pair<String, Integer>> detectedTokens = new ArrayList<>();
    private Map<String, Integer> PIF = new HashMap<String, Integer>();
    private Map<Integer, String> ST = new HashMap<Integer, String>();
    private List<String> specialRelational = new ArrayList<String>();
    private List<String> regularRelational = new ArrayList<String>();
    private String stringConstant = "";
    private String charConstant = "";
    private int currentLine = 0;
    private int capacity = 97;
    private CustomHashTable hashTable = new CustomHashTable(capacity);

    public CustomScanner(String fileName) {
       this.fileName = fileName;
       this.specialRelational.add(">=");
       this.specialRelational.add("<=");
       this.specialRelational.add("==");
       this.regularRelational.add(">");
       this.regularRelational.add("<");
       this.readTokens();
       this.readSeparators();
    }

    private void readTokens() {
       try {
          File myObj = new File("scanner_input/token");
          Scanner myReader = new Scanner(myObj);
          while (myReader.hasNextLine()) {
             String data = myReader.nextLine();
             tokenList.add(data);
          }
          myReader.close();
       } catch (FileNotFoundException e) {
          System.out.println("An error occurred.");
          e.printStackTrace();
       }
    }

    private void readSeparators() {
       try {
          File myObj = new File("scanner_input/token");
          Scanner myReader = new Scanner(myObj);
          for (int i = 0; i < 28; i++){
             String data = myReader.nextLine();
             separatorList.add(data);
          }
          myReader.close();
       } catch (FileNotFoundException e) {
          System.out.println("An error occurred.");
          e.printStackTrace();
       }
    }

    private Boolean isConstant(String token) {
        return token.matches("\\-?[1-9]+[0-9]*|0")
                || token.matches("\"[a-zA-Z0-9 _]+\"")
                || token.equals("True")
                || token.equals("False");
    }

    private Boolean isIdentifier(String token){
        return token.matches("(^[a-zA-Z][a-zA-Z0-9 _]*)");
    }

    private Boolean isStringConstant(String token) {
        if (token.charAt(0) == '"' && token.charAt(token.length() - 2) == '"') {
            String withoutQuote = token.substring(1, token.length() - 2);
            return withoutQuote.length() > 1;
        } else {
            return false;
        }
    }

    private Boolean isCharConstant(String token) {
        if (String.valueOf(token.charAt(0)).equals("'") && String.valueOf(token.charAt(token.length() - 2)).equals("'")) {
            String withoutQuote = token.substring(1, token.length() - 2);
            return withoutQuote.length() <= 1;
        } else {
            return false;
        }
    }

    private Boolean isReservedOperatorSeparator(String myToken) {
        for (String token : this.tokenList) {
            if (myToken.equals(token)) {
                return true;
            }
        }
        return false;
    }

    public void classifyTokens() throws IOException {
        // Pair is token + line of token
        PrintWriter pw = new PrintWriter("/Users/teodoradan/Desktop/Formal-Languages-and-Compiler-Design/Lab3/scanner_output/pif");
        pw.printf("%-20s %s\n", "Token", "ST_Pos");

        Integer lastLine = 0;
        for (Pair<String, Integer> pair: this.detectedTokens) {
            if (isReservedOperatorSeparator(pair.getKey())) {
                pw.printf("%-20s %d\n", pair.getKey(), -1);
            } else if (isIdentifier(pair.getKey()) || isConstant(pair.getKey())
                    || isStringConstant(pair.getKey()) || isCharConstant(pair.getKey())) {
                // index is the position from the ST

                hashTable.insert(pair.getKey());
                int position = hashTable.find(pair.getKey());
                pw.printf("%-20s %d\n", pair.getKey(), position);
            } else {
                System.out.println("LEXICAL ERROR " + pair.getKey() + " AT LINE " + (pair.getValue()));
            }
            lastLine = pair.getValue();
        }

        if (!isStringLexicallyCorrect) {
            System.out.println("LEXICAL ERROR: DOUBLE QUOTES NOT CLOSED AT LINE " + lastLine);
        }
        if (!isCharLexicallyCorrect) {
            System.out.println("LEXICAL ERROR: SINGLE QUOTES NOT CLOSED AT LINE " + lastLine);
        }
        pw.flush();
    }

    public void writeToSymbolTable() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter("/Users/teodoradan/Desktop/Formal-Languages-and-Compiler-Design/Lab3/scanner_output/st");
        pw.printf("%-20s %s\n", "Symbol Table as:", "Hash Table");
        pw.printf("%-20s %s\n", "Symbol", "ST_Pos");
        String[] symTable = hashTable.getSymTable();

        for(int i = 0; i < capacity; i++) {
            if (symTable[i] != null) {
                pw.printf("%-20s %s\n", symTable[i], i);
            }
        }
        pw.flush();
    }

    public void scan() {
       try {
          File myObj = new File(this.fileName);
          Scanner myReader = new Scanner(myObj);
          while (myReader.hasNextLine()) {
             Scanner data = new Scanner(myReader.nextLine());
             currentLine ++;
             while (data.hasNext()) {
                String word = data.next();
                boolean hasSeparator = false;

                for (String separator : separatorList) {
                    // General separator cases
                    if (word.contains(separator)) {
                       hasSeparator = true;
                       this.splitWordWithSeparator(word, separator, currentLine);
                       break;
                    }
                }

                if (!hasSeparator && !isStringLexicallyCorrect) {
                    stringConstant += word + " ";
                }
                if (!hasSeparator && !isCharLexicallyCorrect) {
                    charConstant += word + " ";
                }
                 if (!hasSeparator && isStringLexicallyCorrect && isCharLexicallyCorrect) {
                     detectedTokens.add(new Pair<String, Integer>(word, currentLine));
                 }
             }
          }
          myReader.close();
       } catch (FileNotFoundException e) {
          System.out.println("An error occurred.");
          e.printStackTrace();
       }
    }

    private void splitWordWithSeparator(String word, String separator, Integer line) {
        String[] splitList;

        // Parenthesis
        // Strings
        // Treat RHS and LHS in same function

        boolean specialCase = false;
        boolean containsRelational = false;
        char doubleQuotes = '"';
        String stringDoubleQuotes = String.valueOf(doubleQuotes);

        // String part
        if (word.contains(stringDoubleQuotes) && !isStringLexicallyCorrect) {
            specialCase = true;

            this.isStringLexicallyCorrect = true;
            if (word.charAt(word.length() - 1) == ';') {
                String newWord = word.substring(0, word.length() - 1);
                stringConstant += newWord + " ";
                detectedTokens.add(new Pair<String, Integer>(stringConstant, currentLine));
                detectedTokens.add(new Pair<String, Integer>(";", currentLine));
                stringConstant = "";
                return;
            } else {
                stringConstant += word + " ";
                detectedTokens.add(new Pair<String, Integer>(stringConstant, currentLine));
                stringConstant = "";
                return;
            }
        }

        if (!isStringLexicallyCorrect) {
            specialCase = true;
            stringConstant += word + " ";
            return;
        }

        if (separator.charAt(0) == '"' && isStringLexicallyCorrect) {
            specialCase = true;
            isStringLexicallyCorrect = false;
            stringConstant += word + " ";
            return;
        }

        // Char part
        if (word.contains("'") && !isCharLexicallyCorrect) {
            specialCase = true;

            this.isCharLexicallyCorrect = true;
            if (word.charAt(word.length() - 1) == ';') {
                String newWord = word.substring(0, word.length() - 1);
                charConstant += newWord + " ";
                detectedTokens.add(new Pair<String, Integer>(charConstant, currentLine));
                detectedTokens.add(new Pair<String, Integer>(";", currentLine));
                charConstant = "";
                return;
            } else {
                charConstant += word + " ";
                detectedTokens.add(new Pair<String, Integer>(charConstant, currentLine));
                charConstant = "";
                return;
            }
        }

        if (!isCharLexicallyCorrect) {
            specialCase = true;
            charConstant += word + " ";
            return;
        }

        if (separator.equals("'") && isCharLexicallyCorrect) {
            specialCase = true;
            isCharLexicallyCorrect = false;
            charConstant += word + " ";
            return;
        }

        if (separator.equals("(")) {
            specialCase = true;
            String[] RHS;
            String[] LHS;
            for (String specialSeparator : this.specialRelational) {
                if (word.contains(specialSeparator)) {
                    containsRelational = true;
                    splitList = word.split(specialSeparator);
                    RHS = splitList[0].split("\\(");
                    LHS = splitList[1].split("\\)");
                    detectedTokens.add(new Pair<String, Integer>("(", currentLine));
                    detectedTokens.add(new Pair<String, Integer>(RHS[1], currentLine));
                    detectedTokens.add(new Pair<String, Integer>(specialSeparator, currentLine));
                    detectedTokens.add(new Pair<String, Integer>(LHS[0], currentLine));
                    detectedTokens.add(new Pair<String, Integer>(")", currentLine));
                }
            }
            for (String regularSeparator : this.regularRelational) {
                if (word.contains(regularSeparator) && !containsRelational) {
                    containsRelational = true;
                    splitList = word.split(regularSeparator);
                    RHS = splitList[0].split("\\(");
                    LHS = splitList[1].split("\\)");
                    detectedTokens.add(new Pair<String, Integer>("(", currentLine));
                    detectedTokens.add(new Pair<String, Integer>(RHS[1], currentLine));
                    detectedTokens.add(new Pair<String, Integer>(regularSeparator, currentLine));
                    detectedTokens.add(new Pair<String, Integer>(LHS[0], currentLine));
                    detectedTokens.add(new Pair<String, Integer>(")", currentLine));
                }
            }
            if (!containsRelational) {
                splitList = word.split("\\(");
                detectedTokens.add(new Pair<String, Integer>(separator, currentLine));
                detectedTokens.add(new Pair<String, Integer>(splitList[1], currentLine));
            }
        }

        if (separator.equals(")")) {
            specialCase = true;
            splitList = word.split("\\)");
            detectedTokens.add(new Pair<String, Integer>(splitList[0], currentLine));
            detectedTokens.add(new Pair<String, Integer>(separator, currentLine));
        }

        if (separator.equals("[")) {
            specialCase = true;
            splitList = word.split("\\[");
            detectedTokens.add(new Pair<String, Integer>(splitList[0], currentLine));
            detectedTokens.add(new Pair<String, Integer>(separator, currentLine));
            String[] LHS = splitList[1].split("\\]");
            if (LHS.length == 1) {
                detectedTokens.add(new Pair<String, Integer>(LHS[0], currentLine));
                detectedTokens.add(new Pair<String, Integer>("]", currentLine));
            } else if (LHS.length == 2) {
                detectedTokens.add(new Pair<String, Integer>(LHS[0], currentLine));
                detectedTokens.add(new Pair<String, Integer>("]", currentLine));
                detectedTokens.add(new Pair<String, Integer>(LHS[1], currentLine));
            }
        }

        if (separator.equals("?")) {
            detectedTokens.add(new Pair<String, Integer>(separator, currentLine));
            specialCase = true;
        }

        if (separator.equals("+")) {
            detectedTokens.add(new Pair<String, Integer>(separator, currentLine));
            specialCase = true;
        }

        if (separator.equals(".")) {
            splitList = word.split("\\.");
            detectedTokens.add(new Pair<String, Integer>(splitList[0], currentLine));
            detectedTokens.add(new Pair<String, Integer>(separator, currentLine));
            specialCase = true;
        }

        if (!specialCase) {
            splitList = word.split(separator);
            if (splitList.length == 0) {
                detectedTokens.add(new Pair<String, Integer>(separator, currentLine));
            }

            if (splitList.length == 1) {
                detectedTokens.add(new Pair<String, Integer>(splitList[0], currentLine));
                detectedTokens.add(new Pair<String, Integer>(separator, currentLine));
            }

            if (splitList.length == 2) {
                if (!splitList[0].equals("")) {
                    detectedTokens.add(new Pair<String, Integer>(splitList[0], currentLine));
                }
                detectedTokens.add(new Pair<String, Integer>(separator, currentLine));
                detectedTokens.add(new Pair<String, Integer>(splitList[1], currentLine));
            }
        }
    }
}
