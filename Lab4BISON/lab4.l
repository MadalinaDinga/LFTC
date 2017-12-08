%{
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include "lab4.tab.h"

typedef struct{
    int id1;
    int id2;
    }FIP;

typedef struct{
    char nume[10];
}TSiden;

int fipLength=0;
int constLength = 0;
int identifLength = 0;
int identifCode = 0;
int constCode = 1;
int nrLine = 1;
FIP program[300];

TSiden TSidentif[30];

float TSconstante[30];


void addFip(int id1 , int id2){
    program[fipLength].id1 = id1;
    program[fipLength].id2 = id2;
    fipLength++;
}

void addConst(float atom){
    int found = 0 ;
    int i;
    for(i = 0;i<constLength;i++){
        if (TSconstante[i] == atom){
            found =1;
            addFip(constCode,i);
        }
    }
    if (found == 0){
        TSconstante[constLength]=atom;
        addFip(constCode,constLength);
        constLength++;
    }

}

void addIdentif(char* atom){
	int found = 0 ;
     int i= 0;

    while(i<identifLength){
        int rez = strcmp(TSidentif[i].nume,atom);
        if(strcmp(TSidentif[i].nume,atom)==0){
            found =1;
            addFip(identifCode,i);
        }
        i++;
    }

    if (found == 0){
        strcpy(TSidentif[identifLength].nume,atom);
        addFip(identifCode,identifLength);
        identifLength++;
    }
}

%}

%option noyywrap
DIGIT 		[0-9] 
DIGITNENUL	[1-9]
ID      [a-z][a-z0-9]*

%%
"0"                                     {addConst(atoi( yytext )); return CONSTANT; }
{DIGITNENUL}                            {addConst(atoi( yytext )); return CONSTANT; }
{DIGITNENUL}{DIGIT}+                    {addConst(atoi( yytext )); return CONSTANT; }
{DIGITNENUL}{DIGIT}+"."{DIGIT}*         {addConst(atof( yytext )); return CONSTANT; }
"0""."{DIGIT}*                          {addConst(atof( yytext )); return CONSTANT; }
{DIGITNENUL}"."{DIGIT}*                 {addConst(atof( yytext )); return CONSTANT; }

\identificator                          {addFip(identifCode,0); return IDENTIFICATOR;}
\constanta                              {addFip(constCode,0); return CONSTANT;}
program                               {addFip(2,0); return PROGRAM;}
const                                  {addFip(3,0); return CONST;}
var                                    {addFip(4,0); return VAR;}
begin                                  {addFip(5,0); return BEGINN;}
end                                    {addFip(6,0); return END;}
if                                     {addFip(7,0); return IF;}
else                                     {addFip(8,0); return ELSE;}
for                                     {addFip(9,0); return FOR;}
while                                      {addFip(10,0); return WHILE;}
integer                                     { addFip(11,0); return INTEGER;}
real                                   {addFip(12,0); return REAL;}
array                                     {addFip(13,0); return ARRAY;}
of                                     {addFip(14,0); return OF;}
read                                      {addFip(15,0); return READ;}
write                                      {addFip(16,0); return WRITE;}
call                                     {addFip(17,0); return CALL;}
\(                                     {addFip(18,0); return '(';}
\)                              	{addFip(19,0); return ')';}
\{                                   {addFip(20,0); return '{';}
\}                                    {addFip(21,0); return '}';}
\,                                      {addFip(22,0); return ',';}
\+                                      {addFip(23,0); return '+';}
\-                                      {addFip(24,0); return '-';}
\*                                      {addFip(25,0); return '*';}
\/                                      {addFip(26,0); return '/';}
\%                                    {addFip(27,0); return '%';}
\>                                     {addFip(28,0); return GT;}
\<                               	{ addFip(29,0); return LT;}
\>=                                    {addFip(30,0); return GE;}
\<=                                      {addFip(31,0); return LE;}
\:=                                    { addFip(32,0); return ASSIGN;}
\<>                                     {addFip(33,0); return NEQ;}
\=                                     {addFip(34,0); return EQ;}
\[                                    {addFip(35,0); return '[';}
\]                                    {addFip(36,0); return ']';}
\.                                  { addFip(37,0); return '.';}
\:                                    {addFip(38,0); return ':';}
\OR                                   { addFip(39,0); return OR;}
\AND                                    {addFip(40,0); return AND;}

{ID}                                   { addIdentif(yytext); return IDENTIFICATOR;}

[\n]                                  ++nrLine;
[ \t]+             ;/* eat up whitespace */
. {
  if(yytext[0] != 13) {
	printf("Illegal token %s si %d !",yytext,yytext[0]);
	}
  }
%%