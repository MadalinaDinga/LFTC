I.
flex lab3.lx
gcc lex.yy.c
./a.exe {1.txt,2.txt,3.txt}

II.
$ flex lab3.lx => lex.yy.c
$ gcc lex.yy.c -o exeFile => exe
Linux                             Windows
$ ./ exeFile (<stdin)           | > exeFile.exe 1.txt 2.txt 3.txt
$ ./ exeFile <fisier_intrare    | 


LEX = computer program that generates lexical analysers( tokens- the words in a language)
I. lexical analysis( tokeniser/ lexer/ scanner)=> produce the words in a language
FA( finite automata)- recognise tokens
=> literal table( keywords)
=> ST( identifiers- variables, constants, procedure names...)
II. syntactic analysis - check the rules of a language, the shape/ form of the sentence
CFG( context free grammars) 
	- using BNF: 
	phrases <>( non-terminal symbols), 
	tokens( terminal symbols),
	metasymbol ::=( by definition)
III. semantic analysis
type correctness of expressions
declaration prior to use
Error handler( syntax errors...)
PIF( Program Internal Form)
LEX = computer program that generates lexical analysers( scanners/ lexers)
FLEX( Fast LEXical analyzer generator) = a tool for generating scanners
- reads an input stream specifying the lexer .lex
- outputs C source file implementing the lexer lex.yy.c
lex.yy.c is compiled, linked with the -lfl library => a.out executable( the scanner)
The executable analyzes the input stream and transforms it into a sequence of tokens.

1. Format:
definitions // name definition
%%
rules // pattern definition
%%
user code  // yylex() routine

man flex

