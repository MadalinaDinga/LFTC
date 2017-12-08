flex lab4.lxi
bison -d lab4.y
gcc lex.yy.c lab4.tab.c
./a.exe ["circle.txt"|"gcd.txt"|"sum.txt"]

1. Lexical analysis: .l or .lxi
	lex/ flex - generate scanners( lexical analyzer/ lexer/ tokenizer)
.l spec file --flex--> lex.yy.c
	- definition section %{ C code }% 
	%option noyywrap
	%%
	- rules section:  [0-9]+ match a string of one or more digits
	%%
	- C code section
	yylex() routine
- flex matches the token with the longest match

2. Syntactical analysis: .y 
	yacc/ bison - generate parsers( syntactic analyser)
.y spec file --bison--> somename.tab.h
		    --> somename.tab.c
	- definition section %{ C code }% 
		%token <symp> NAME/ token <dval> NUMBER - declaration of tokens
		%left  '-', '+'/ %right/ %nonassoc - operator precedence and associativity
		%type <dval> expression - defined non-terminal name
	%%
	- rules section 
		statement_list: statement '\n'
 				| statement_list statement '\n'
		( a statement list is made up of a statement OR a statement list 
		followed by a statement)

		statement: NAME '=' expression { $1->value = $3; }
 				| expression { printf("= %g\n", $1); }
		 ( the first production is an assignment statement, the
		second is a simple expression)

		expression: NUMBER
 			| NAME { $$ = $1->value; } 
		( An expression is a number or a name. 
		The executable statement { } says to return the value.
		When the rule is matched, the executable statement is run.
		The numbers in the executable statement correspond to 
		the tokens listed in the production.)
		
	%%
	- C code section
	
- the parser reads a series of tokens and 
tries to determine the grammatical structure 
with respect to a given grammar.

– a grammar is a set of formation rules for strings in a
formal language. The rules describe how to
form strings from the language's alphabet
(tokens) that are valid according to the
language's syntax

3. Semantic analyzer


	