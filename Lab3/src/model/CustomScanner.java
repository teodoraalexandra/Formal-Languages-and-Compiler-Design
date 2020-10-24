package model;

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
    //TODO: Implement the scanner (lexical analyzer)
}
