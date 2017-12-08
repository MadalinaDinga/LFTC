%{
    #include <string.h>
    #include <stdio.h>
    #include <stdlib.h>

    extern int yylex();
    extern int yyparse();
    extern FILE *yyin;
    extern int nrLine;
    void yyerror(const char *s);
%}

%token IDENTIFICATOR
%token CONSTANT
%token PROGRAM
%token CONST
%token VAR
%token BEGINN
%token END
%token IF
%token ELSE
%token FOR
%token WHILE
%token INTEGER
%token REAL
%token ARRAY
%token OF
%token READ
%token WRITE
%token CALL
%token GT
%token LT
%token GE
%token LE
%token ASSIGN
%token NEQ
%token EQ
%token OR
%token AND

%%     

program: PROGRAM IDENTIFICATOR paramlist BEGINN statementlist END

paramlist: param | param paramlist

param: VAR IDENTIFICATOR ':' type | CONST IDENTIFICATOR ':' type

type: simpletype|comptype

simpletype: INTEGER | REAL

comptype: ARRAY '[' CONSTANT ']' OF simpletype

statementlist: instructiune | instructiune statementlist

instructiune: atribuire | io | if | while | for | call

atribuire: IDENTIFICATOR ASSIGN expresie

expresie: term | expresie '+' term | expresie '-' term

term: factor | factor '*' term | factor '/' term |factor '%' term

factor: CONSTANT | IDENTIFICATOR | identifCompus | '(' expresie ')'

identifCompus: IDENTIFICATOR '[' chestie ']'

chestie: IDENTIFICATOR | CONSTANT

io: READ '(' IDENTIFICATOR ')' | READ '(' identifCompus ')' | WRITE '(' IDENTIFICATOR ')' |  WRITE '(' identifCompus ')'

if: IF condition BEGINN statementlist END | IF condition BEGINN statementlist END ELSE BEGINN statementlist END

condition: expresie relatie expresie

relatie: LE | LT | GE | GT | EQ | NEQ

while: WHILE condition BEGINN statementlist END

for: FOR IDENTIFICATOR ASSIGN chestie ',' chestie BEGINN statementlist END | FOR ':' IDENTIFICATOR ASSIGN chestie ',' chestie ',' chestie BEGINN statementlist END

call: CALL ':' IDENTIFICATOR '(' listaParam ')'

listaParam:  IDENTIFICATOR ':' type | expresie ':' type | IDENTIFICATOR ':' type listaParam | expresie ':' type listaParam

%%

int main(int argc, char *argv[]) {
    ++argv, --argc; /* skip over program name */ 
    
    // sets the input for flex file
    if (argc > 0) 
        yyin = fopen(argv[0], "r"); 
    else 
        yyin = stdin; 
    
    //read each line from the input file and process it
    while (!feof(yyin)) {
        yyparse();
    }
    printf("The file is syntactly correct!\n");
    return 0;
}

void yyerror(const char *s) {
    printf("Error: %s at line -> %d ! \n", s, nrLine);
    exit(1);
}