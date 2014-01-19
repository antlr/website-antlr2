header {
	import java.util.*;
}

class TinyCParser extends Parser;

{
TokenStreamRewriteEngine engine;

public TinyCParser(TokenStreamRewriteEngine lexer) {
  this(lexer,1);
  engine = lexer;
}
}


program
	:	( declaration )* EOF
	;

declaration
	:	(globalVariable) => globalVariable
	|	function
	;

declarator
	:	id:ID
	|	STAR id2:ID
	;

variable
	:	type declarator SEMI
	;

/** Convert "int foo;" into "extern int foo;" */
globalVariable
    :   {engine.insertBefore(LT(1),"extern ");}
        variable
    ;

/** Convert "int foo() {...}" into "extern int foo();" */
function
{
    int rcurly=0;
}
	:	{engine.insertBefore(LT(1),"extern ");}
        type id:ID LPAREN
		(formalParameter (COMMA formalParameter)*)?
		RPAREN
        block[true]
	;

formalParameter
	:	type declarator
	;

type:	"int" | "char" | ID ;

block[boolean functionLevel]
	:	a:LCURLY ( statement )* b:RCURLY
        {
            if ( functionLevel ) {
                int prevTokenIndex = ((TokenWithIndex)a).getIndex()-1;
                TokenWithIndex prevToken = engine.getToken(prevTokenIndex);
                if ( prevToken.getType()==RPAREN ) {
                    engine.replace(a,b,";");
                }
                else {
                    engine.replace(prevToken,b,";"); // replace whitespace too
                }
            }
        }
	;

statement
	:	(variable)=>variable
	|	expr SEMI
	|	"if" LPAREN expr RPAREN statement
		( "else" statement )?
	|	"while" LPAREN expr RPAREN statement
	|	block[false]
	;

expr:	assignExpr
	;

assignExpr
	:	aexpr (ASSIGN assignExpr)?
	;

aexpr
	:	mexpr (PLUS mexpr)*
	;

mexpr
	:	atom (STAR atom)*
	;

atom:	ID
	|	INT
	|	CHAR_LITERAL
	|	STRING_LITERAL
	;

class TinyCLexer extends Lexer;
options {
	k=2;
	charVocabulary = '\3'..'\377';
}

WS	:	(' '
	|	'\t'
	|	'\n'	{newline();}
	|	'\r')+
	;

SL_COMMENT : 
	"//" 
	(~'\n')* '\n'
	{ $setType(Token.SKIP); newline(); }
	;

ML_COMMENT
	:	"/*"
		(	{ LA(2)!='/' }? '*'
		|	'\n' { newline(); }
		|	~('*'|'\n')
		)*
		"*/"
			{ $setType(Token.SKIP); }
	;


LPAREN
	:	'('
	;

RPAREN
	:	')'
	;

LCURLY:	'{'
	;

RCURLY:	'}'
	;

STAR:	'*'
	;

PLUS:	'+'
	;

ASSIGN
	:	'='
	;

SEMI:	';'
	;

COMMA
	:	','
	;

CHAR_LITERAL
	:	'\'' (options {greedy=false;}:.)* '\''
	;

STRING_LITERAL
	:	'"' (options {greedy=false;}:.)* '"'
	;

protected
DIGIT
	:	'0'..'9'
	;

INT	:	(DIGIT)+
	;

ID	:	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*
	;


