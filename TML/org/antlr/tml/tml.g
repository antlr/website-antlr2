/*
 [Adapted from BSD licence]
 Copyright (c) 2002 Terence Parr
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.
 3. The name of the author may not be used to endorse or promote products
    derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
header {
package org.antlr.tml;
import java.util.*;
import java.io.*;
}

/** Parse and translate TML input.  All lexers for an input and included text
 *  share the same context but each lexer can have their own translator target.
 *  Most functionality is in the DefaultTMLEngine.
 */
class TMLLexer extends Lexer;

options {
	classHeaderPrefix = "public abstract";
    charVocabulary='\003'..'\377';
    k=5; // see %def( at left edge
    filter=TEXT;
}

{

	/** Used by sublexers that handle plug in data; shares context with original */
	public TMLLexer(Reader in, TMLContext context) {
		this(in);
		setContext(context);
	}

    /** Tracks variables and such; later track line number etc... */
    protected TMLContext context = new TMLContext(this);

    /** Each lexer/translator can have it's own translator */
	public TMLTranslator translator = new NOPTarget(); // make sure not null

    /** Can't do some things in tables like bullet lists */
    protected boolean inTable=false;

	public TMLTranslator getTranslator() {
		return translator;
	}

    public TMLContext getContext() {
        return context;
    }

	public void setContext(TMLContext context) {
		this.context = context;
	}

	/** Begin on-the-fly syntax-directed translation */
	public abstract void translate(TMLTranslator translator)
        throws TokenStreamException;
    protected abstract void closeAllLists();
    protected abstract void closeAllLists(int level);
    protected abstract void closeList();
    protected abstract void captureText();
    protected abstract void ul(int level);
    protected abstract void ol(int level);
	protected abstract void closeSectionsToLevel(int level);
	protected abstract void section(String text, int level);
	protected abstract void title(String title);
	protected abstract void define(String id, String value);
	protected abstract void plugin(String id, Vector args);
	protected abstract void variable(String id);
}

BOLD
    :   {captureText();} '*'! (~'*')+ '*'! {translator.bold($getText);}
    ;

ITALIC
    :   {captureText();} '_'! (~'_')+ '_'! {translator.italic($getText);}
    ;

TT  :   {captureText();} '{'! (~'}')+ '}'! {translator.tt($getText);}
    ;

/** A link looks like @(url,title) or @(url),
 *  which is same as http://foo.com.  An anchor ref to an image,
 *  table or section will be @table(name).
 */
LINK
{
    String url=null, title=null;
}
    :   {getLine()!=1}? // distinguish from title
		{captureText();}
		(	"@("!
			(~(')'|','))+    {url=$getText; $setText("");}
			  ( ','! (~(')'))+ {title=$getText;} )?
			')'! {translator.link(url,title);}
		|	"http://" (~(' '|'\n'))+
			{translator.link($getText,null);}
		)
    ;

/** First line of outermost file (stdin usually) is title */
TITLE
    :   {getLine()==1&&getColumn()==1&&!context.isNestedTranslator()}?
		(options{greedy=true;}:' '!|'\t'!)* (NOT_SPECIAL_CHAR)+
		{title($getText);}
    ;

protected
NOT_SPECIAL_CHAR
	:	~('{' | '}' | '@' | '*' | '_' | '#' | '\n' | '-' )
	;

BR  :   '\\'! '\n'!   {captureText(); translator.linebreak();}
    ;

BLANK
    :   '\n'! '\\'! '\n'!   {captureText(); translator.blankline();}
    ;

PLUGIN
{
    Vector args=null;
}
    :   (
            options {
                // %def matched also by 3rd alternative %ID (tell it to shutup)
                generateAmbigWarnings=false;
            }
        :
            "%def"! '(' id:ID ',' WS a:ARG ')'
            {define(id.getText(),a.getText());}
        |   "%output"! WS! out:CODE
		    {captureText(); translator.verbatim(out.getText());}
        |   '%' id2:ID
            (   '(' ')'              {plugin(id2.getText(), null);}
            |   '(' args=ARGLIST ')' {plugin(id2.getText(), args);}
            |	WS! arg:CODE		 {Vector v=new Vector();
									  v.addElement(arg.getText());
									  plugin(id2.getText(), v);
									 }
            |   {variable(id2.getText());}
            )
        )
    ;

protected
ARGLIST returns [Vector args=new Vector()]
    :   a1:ARG {args.addElement(a1.getText());}
        ( ',' WS a2:ARG {args.addElement(a2.getText());} )*
    ;

protected
ARG :   STRING
    |   TEXT_TIL_END_ARG
    |   '%' id:ID
        {String value = (context.getVariable(id.getText())).toString();
         $setText(value);
        }
    ;

protected
STRING
    :   '"'! (~'"')* '"'!
    ;

protected
TEXT_TIL_END_ARG
    :   ( ~('"'|','|')'|'%') )+
    ;

protected
ID  :   ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*
    ;

ESC :   '\\'! c:~'\n' {context.textBetweenMarkup.append(c);}
    ;

protected
ESC2:   '\\'! c:~'\n'
    ;

LINE_START_THEN_MARKUP
    :   {getColumn()==1}? LEFT_EDGE_MARKUP
    ;

BLANK_LINE_THEN_MARKUP
    :   "\n\n"! {context.textBetweenMarkup.append('\n');} LEFT_EDGE_MARKUP
    ;

protected
LEFT_EDGE_MARKUP
{
    int level = TMLContext.LOWEST_LEVEL;
}
    :
        (
            {!inTable}?
            ("  " {level++;} )*
            (   "o "!       {captureText(); ul(level);}
            |   "1 "!       {captureText(); ol(level);}
            )
        |   BLOCKQUOTE  {captureText(); closeAllLists();
                         translator.blockquote($getText);}
        |   CODE        {captureText(); translator.code($getText);}
        |   SECTION
        |   TABLE       {inTable=true;}
        |   END_TABLE   {inTable=false;}
        )
    ;

protected
BLOCKQUOTE
    :   "  \""! ('\n'{newline();}|~('\n'|'"'))+ '"'!
    ;

protected
CODE:   "<<"! (options {greedy=false;}:'\n'{newline();}|ESC2|.)* ">>"!
    ;

protected
SECTION
{
captureText();
}
    :   "# "! (~('#'|'\n'))+     {section($getText, 4);}
    |   "## "! (~('#'|'\n'))+    {section($getText, 3);}
    |   "### "! (~('#'|'\n'))+   {section($getText, 2);}
    |   "#### "! (~('#'|'\n'))+  {section($getText, 1);}
    ;

protected
TABLE
    :   '['! {context.inTable=true; captureText(); translator.begin_table();}
    ;

protected
END_TABLE
    :   ']'! {context.inTable=false; captureText(); translator.end_table();}
    ;

COL_SEP
    :   {context.inTable}? '|'! {captureText(); translator.col();}
    ;

ROW_SEP
    :   {context.inTable}? "----" {captureText(); translator.row();}
    ;

/** The catch-all "else" rule.  Anything not matched as a tag, gets
 *  matched here.  The chars are buffered up until the next tag is
 *  recognized at which point the translator.text() method gets the
 *  text.
 */
protected
TEXT:   "\n\n"  {
                newline(); newline();
                captureText();
                closeAllLists();
                translator.paragraph();
                }
    |   '\n'    {newline(); context.textBetweenMarkup.append('\n');}
    |   c:~'\n' {context.textBetweenMarkup.append(c);}
    ;

protected
WS! :   (options {greedy=true;}:' '|'\t'|'\n'{newline();})*
    ;

protected
WS2!:   (options {greedy=true;}:' '|'\t'|'\n'{newline();})+
    ;

