#!/bin/sh
flex spec.lxi
yacc -d spec.y
gcc -o result lex.yy.c y.tab.c -w -ll
./result source.in
