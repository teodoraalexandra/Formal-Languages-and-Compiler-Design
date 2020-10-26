package model;

import java.io.File;
import java.io.FileNotFoundException;
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
    private boolean isLexicallyCorrect = true;
    private List<String> tokenList = new ArrayList<String>();
    private List<String> separatorList = new ArrayList<String>();
    private List<String> detectedTokens = new ArrayList<String>();
    private Map<String, Integer> PIF = new HashMap<String, Integer>();
    private Map<Integer, String> ST = new HashMap<Integer, String>();
    private List<String> specialRelational = new ArrayList<String>();
    private List<String> regularRelational = new ArrayList<String>();
    private int index = 0;
    private String stringConstant = "";

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
          for (int i = 0; i < 27; i++){
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
        return token.matches("(^[a-zA-Z][a-zA-Z0-9]*)");
    }

    private Boolean isReservedOperatorSeparator(String myToken) {
        for (String token : this.tokenList) {
            if (myToken.equals(token)) {
                return true;
            }
        }
        return false;
    }

    public void classifyTokens() {
        for (String token : this.detectedTokens) {
            if (isReservedOperatorSeparator(token)) {
                System.out.println("PIF " + token + " -1");
            } else if (isIdentifier(token) || isConstant(token)) {
                // index is the position from the ST
                System.out.println("PIF " + token + " " + index);
                index++;
            } else {
                System.out.println("LEXICAL ERROR" + token);
            }
        }
    }

    public void scan() {
       try {
          File myObj = new File(this.fileName);
          Scanner myReader = new Scanner(myObj);
          while (myReader.hasNextLine()) {
             Scanner data = new Scanner(myReader.nextLine());
             while (data.hasNext()) {
                String word = data.next();

                boolean hasSeparator = false;

                for (String separator : separatorList) {
                    // General separator cases
                    if (word.contains(separator)) {
                       hasSeparator = true;
                       this.splitWordWithSeparator(word, separator);
                       break;
                    }
                }
                if (!hasSeparator && isLexicallyCorrect) {
                    detectedTokens.add(word);
                }
                if (!hasSeparator && !isLexicallyCorrect) {
                    stringConstant += word + " ";
                }
             }
          }
          myReader.close();
       } catch (FileNotFoundException e) {
          System.out.println("An error occurred.");
          e.printStackTrace();
       }
    }

    private void splitWordWithSeparator(String word, String separator) {
        String[] splitList;

        // Parenthesis
        // Strings
        // Treat RHS and LHS in same function

        boolean specialCase = false;
        boolean containsRelational = false;

        if (separator.charAt(0) == '"' && !isLexicallyCorrect) {
            specialCase = true;
            isLexicallyCorrect = true;
            if (word.charAt(word.length() - 1) == ';') {
                String newWord = word.substring(0, word.length() - 1);
                stringConstant += newWord + " ";
                detectedTokens.add(stringConstant);
                detectedTokens.add(";");
            } else {
                stringConstant += word + " ";
                detectedTokens.add(stringConstant);
            }
        }

        if (!isLexicallyCorrect) {
            specialCase = true;
            stringConstant += word + " ";
        }

        if (separator.charAt(0) == '"' && isLexicallyCorrect) {
            specialCase = true;
            isLexicallyCorrect = false;
            stringConstant += word + " ";
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
                    detectedTokens.add("(");
                    detectedTokens.add(RHS[1]);
                    detectedTokens.add(specialSeparator);
                    detectedTokens.add(LHS[0]);
                    detectedTokens.add(")");
                }
            }
            for (String regularSeparator : this.regularRelational) {
                if (word.contains(regularSeparator) && !containsRelational) {
                    containsRelational = true;
                    splitList = word.split(regularSeparator);
                    RHS = splitList[0].split("\\(");
                    LHS = splitList[1].split("\\)");
                    detectedTokens.add("(");
                    detectedTokens.add(RHS[1]);
                    detectedTokens.add(regularSeparator);
                    detectedTokens.add(LHS[0]);
                    detectedTokens.add(")");
                    //System.out.println("(");
                    //System.out.println(RHS[1]);
                    //System.out.println(regularSeparator);
                    //System.out.println(LHS[0]);
                    //System.out.println(")");
                }
            }
            if (!containsRelational) {
                splitList = word.split("\\(");
                detectedTokens.add(separator);
                detectedTokens.add(splitList[1]);
                //System.out.println(separator);
                //System.out.println(splitList[1]);
            }
        }

        if (separator.equals(")")) {
            specialCase = true;
            splitList = word.split("\\)");
            detectedTokens.add(splitList[0]);
            detectedTokens.add(separator);
            //System.out.println(splitList[0]);
            //System.out.println(separator);
        }

        if (separator.equals("[")) {
            specialCase = true;
            splitList = word.split("\\[");
            detectedTokens.add(splitList[0]);
            detectedTokens.add(separator);
            //System.out.println(splitList[0]);
            //System.out.println(separator);
            String[] LHS = splitList[1].split("\\]");
            if (LHS.length == 1) {
                detectedTokens.add(LHS[0]);
                detectedTokens.add("]");
                //System.out.println(LHS[0]);
                //System.out.println("]");
            } else if (LHS.length == 2) {
                detectedTokens.add(LHS[0]);
                detectedTokens.add("]");
                detectedTokens.add(LHS[1]);
                //System.out.println(LHS[0]);
                //System.out.println("]");
                //System.out.println(LHS[1]);
            }
        }

        if (separator.equals("?")) {
            detectedTokens.add(separator);
            //System.out.println(separator);
            specialCase = true;
        }

        if (separator.equals("+")) {
            detectedTokens.add(separator);
            //System.out.println(separator);
            specialCase = true;
        }

        if (separator.equals(".")) {
            splitList = word.split("\\.");
            detectedTokens.add(splitList[0]);
            detectedTokens.add(separator);
            //System.out.println(splitList[0]);
            //System.out.println(separator);
            specialCase = true;
        }

        if (!specialCase) {
            splitList = word.split(separator);
            if (splitList.length == 0) {
                detectedTokens.add(separator);
                //System.out.println(separator);
            }
            if (splitList.length == 1) {
                detectedTokens.add(splitList[0]);
                detectedTokens.add(separator);
                //System.out.println(splitList[0]);
                //System.out.println(separator);
            }
            if (splitList.length == 2) {
                if (!splitList[0].equals("")) {
                    detectedTokens.add(splitList[0]);
                    //System.out.println(splitList[0]);
                }
                detectedTokens.add(separator);
                detectedTokens.add(splitList[1]);
                //System.out.println(separator);
                //System.out.println(splitList[1]);
            }
        }
    }
}
