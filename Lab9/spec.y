%{
#include <stdio.h>
#include <stdlib.h>

#define YYDEBUG 1
%}

%token no
%token wow
%token gata
%token variabila
%token boolean
%token chaar
%token numar
%token reaal
%token citeste
%token listeaza
%token este
%token atunci
%token sfarsit
%token altfel
%token cattimp
%token executa
%token bucla
%token start
%token stop
%token pas
%token IDENTIFIER
%token CONSTANT
%token COLON
%token SEMI_COLON
%token COMA
%token DOT
%token PLUS
%token MINUS
%token MULTIPLY
%token DIVISION
%token FLOOR_DIVISION
%token MODULO
%token LEFT_ROUND_BRACKETS
%token RIGHT_ROUND_BRACKETS
%token LEFT_SQUARE_BRACKETS
%token RIGHT_SQUARE_BRACKETS
%token LEFT_CURLY_BRACKETS
%token RIGHT_CURLY_BRACKETS
%token QUESTION_MARK
%token LESS_THAN
%token GREATER_THAN
%token LESS_OR_EQUAL_THAN
%token GREATER_OR_EQUAL_THAN
%token DIFFERENT
%token EQUAL
%token ASSIGNMENT
%token AND_OPERATOR
%token OR_OPERATOR

%start program

%%

program : no declarationList wow compoundStatement gata DOT ;
declarationList : declaration SEMI_COLON declarationList | declaration ;
declaration : variabila type IDENTIFIER ;
type : primitiveType | primitiveType arrayDeclaration ;
primitiveType : boolean | chaar | numar | reaal ;
arrayDeclaration : LEFT_SQUARE_BRACKETS CONSTANT RIGHT_SQUARE_BRACKETS ;
compoundStatement : LEFT_CURLY_BRACKETS statementList RIGHT_CURLY_BRACKETS ;
statementList : statement SEMI_COLON statementList | statement ;
statement : simpleStatement | structStatement ;
simpleStatement : assignmentStatement | ioStatement ;
assignmentStatement : IDENTIFIER ASSIGNMENT expression ;
expression : term | term PLUS expression | term MINUS expression | term MULTIPLY expression | term DIVISION expression | term FLOOR_DIVISION expression | term MODULO expression | LEFT_ROUND_BRACKETS expression RIGHT_SQUARE_BRACKETS ;
term : IDENTIFIER | CONSTANT ;
ioStatement : citeste type IDENTIFIER | listeaza IDENTIFIER | listeaza CONSTANT ;
structStatement : ifStatement | whileStatement | forStatement ;
ifStatement : este LEFT_ROUND_BRACKETS conditionList RIGHT_ROUND_BRACKETS QUESTION_MARK atunci statementList sfarsit | este LEFT_ROUND_BRACKETS conditionList RIGHT_ROUND_BRACKETS QUESTION_MARK atunci statementList sfarsit altfel statementList sfarsit;
whileStatement : cattimp conditionList executa statementList sfarsit ;
forStatement : bucla IDENTIFIER start CONSTANT stop IDENTIFIER statementList sfarsit | bucla IDENTIFIER start IDENTIFIER stop IDENTIFIER pas IDENTIFIER statementList sfarsit;
conditionList: condition | condition AND_OPERATOR condition | condition OR_OPERATOR condition ;
condition : expression relation expression ;
relation : LESS_THAN | GREATER_THAN | LESS_OR_EQUAL_THAN | GREATER_OR_EQUAL_THAN | DIFFERENT | EQUAL ;

%%

yyerror(char *s)
{
  printf("%s\n", s);
}

extern FILE *yyin;

main(int argc, char **argv)
{
  if (argc > 1)
    yyin = fopen(argv[1], "r");
  if ( (argc > 2) && ( !strcmp(argv[2], "-d") ) )
    yydebug = 1;
  if ( !yyparse() )
    fprintf(stderr,"\t It seems that you do not have any errors: good job :) \n");
}
