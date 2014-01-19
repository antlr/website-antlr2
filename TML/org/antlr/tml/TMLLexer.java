// $ANTLR 2.7.3: "tml.g" -> "TMLLexer.java"$

package org.antlr.tml;
import java.util.*;
import java.io.*;

import java.io.InputStream;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.TokenStreamRecognitionException;
import antlr.CharStreamException;
import antlr.CharStreamIOException;
import antlr.ANTLRException;
import java.io.Reader;
import java.util.Hashtable;
import antlr.CharScanner;
import antlr.InputBuffer;
import antlr.ByteBuffer;
import antlr.CharBuffer;
import antlr.Token;
import antlr.CommonToken;
import antlr.RecognitionException;
import antlr.NoViableAltForCharException;
import antlr.MismatchedCharException;
import antlr.TokenStream;
import antlr.ANTLRHashString;
import antlr.LexerSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.SemanticException;

/** Parse and translate TML input.  All lexers for an input and included text
 *  share the same context but each lexer can have their own translator target.
 *  Most functionality is in the DefaultTMLEngine.
 */
public abstract class TMLLexer extends antlr.CharScanner implements TMLLexerTokenTypes, TokenStream
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
public TMLLexer(InputStream in) {
	this(new ByteBuffer(in));
}
public TMLLexer(Reader in) {
	this(new CharBuffer(in));
}
public TMLLexer(InputBuffer ib) {
	this(new LexerSharedInputState(ib));
}
public TMLLexer(LexerSharedInputState state) {
	super(state);
	caseSensitiveLiterals = true;
	setCaseSensitive(true);
	literals = new Hashtable();
}

public Token nextToken() throws TokenStreamException {
	Token theRetToken=null;
tryAgain:
	for (;;) {
		Token _token = null;
		int _ttype = Token.INVALID_TYPE;
		setCommitToPath(false);
		int _m;
		_m = mark();
		resetText();
		try {   // for char stream error handling
			try {   // for lexical error handling
				switch ( LA(1)) {
				case '*':
				{
					mBOLD(true);
					theRetToken=_returnToken;
					break;
				}
				case '_':
				{
					mITALIC(true);
					theRetToken=_returnToken;
					break;
				}
				case '{':
				{
					mTT(true);
					theRetToken=_returnToken;
					break;
				}
				default:
					if (((LA(1)=='@'||LA(1)=='h') && (LA(2)=='('||LA(2)=='t') && (_tokenSet_0.member(LA(3))) && ((LA(4) >= '\u0003' && LA(4) <= '\u00ff')) && (true))&&(getLine()!=1)) {
						mLINK(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='\\') && (LA(2)=='\n')) {
						mBR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='\n') && (LA(2)=='\\')) {
						mBLANK(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='%') && (_tokenSet_1.member(LA(2))) && (true) && (true) && (true)) {
						mPLUGIN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='\\') && (_tokenSet_2.member(LA(2))) && (true) && (true) && (true)) {
						mESC(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='\n') && (LA(2)=='\n')) {
						mBLANK_LINE_THEN_MARKUP(true);
						theRetToken=_returnToken;
					}
					else if (((_tokenSet_3.member(LA(1))) && (true) && (true) && (true) && (true))&&(getLine()==1&&getColumn()==1&&!context.isNestedTranslator())) {
						mTITLE(true);
						theRetToken=_returnToken;
					}
					else if (((_tokenSet_4.member(LA(1))) && (true) && (true) && (true) && (true))&&(getColumn()==1)) {
						mLINE_START_THEN_MARKUP(true);
						theRetToken=_returnToken;
					}
					else if (((LA(1)=='|') && (true) && (true) && (true) && (true))&&(context.inTable)) {
						mCOL_SEP(true);
						theRetToken=_returnToken;
					}
					else if (((LA(1)=='-'))&&(context.inTable)) {
						mROW_SEP(true);
						theRetToken=_returnToken;
					}
				else {
					if (LA(1)==EOF_CHAR) {uponEOF(); _returnToken = makeToken(Token.EOF_TYPE);}
				else {
					commit();
					try {mTEXT(false);}
					catch(RecognitionException e) {
						// catastrophic failure
						reportError(e);
						consume();
					}
					continue tryAgain;
				}
				}
				}
				commit();
				if ( _returnToken==null ) continue tryAgain; // found SKIP token
				_ttype = _returnToken.getType();
				_ttype = testLiteralsTable(_ttype);
				_returnToken.setType(_ttype);
				return _returnToken;
			}
			catch (RecognitionException e) {
				if ( !getCommitToPath() ) {
					rewind(_m);
					resetText();
					try {mTEXT(false);}
					catch(RecognitionException ee) {
						// horrendous failure: error in filter rule
						reportError(ee);
						consume();
					}
					continue tryAgain;
				}
				throw new TokenStreamRecognitionException(e);
			}
		}
		catch (CharStreamException cse) {
			if ( cse instanceof CharStreamIOException ) {
				throw new TokenStreamIOException(((CharStreamIOException)cse).io);
			}
			else {
				throw new TokenStreamException(cse.getMessage());
			}
		}
	}
}

	public final void mBOLD(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BOLD;
		int _saveIndex;
		
		captureText();
		_saveIndex=text.length();
		match('*');
		text.setLength(_saveIndex);
		{
		int _cnt3=0;
		_loop3:
		do {
			if ((_tokenSet_5.member(LA(1)))) {
				matchNot('*');
			}
			else {
				if ( _cnt3>=1 ) { break _loop3; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
			}
			
			_cnt3++;
		} while (true);
		}
		_saveIndex=text.length();
		match('*');
		text.setLength(_saveIndex);
		translator.bold(new String(text.getBuffer(),_begin,text.length()-_begin));
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mITALIC(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ITALIC;
		int _saveIndex;
		
		captureText();
		_saveIndex=text.length();
		match('_');
		text.setLength(_saveIndex);
		{
		int _cnt6=0;
		_loop6:
		do {
			if ((_tokenSet_6.member(LA(1)))) {
				matchNot('_');
			}
			else {
				if ( _cnt6>=1 ) { break _loop6; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
			}
			
			_cnt6++;
		} while (true);
		}
		_saveIndex=text.length();
		match('_');
		text.setLength(_saveIndex);
		translator.italic(new String(text.getBuffer(),_begin,text.length()-_begin));
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mTT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = TT;
		int _saveIndex;
		
		captureText();
		_saveIndex=text.length();
		match('{');
		text.setLength(_saveIndex);
		{
		int _cnt9=0;
		_loop9:
		do {
			if ((_tokenSet_7.member(LA(1)))) {
				matchNot('}');
			}
			else {
				if ( _cnt9>=1 ) { break _loop9; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
			}
			
			_cnt9++;
		} while (true);
		}
		_saveIndex=text.length();
		match('}');
		text.setLength(_saveIndex);
		translator.tt(new String(text.getBuffer(),_begin,text.length()-_begin));
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
/** A link looks like @(url,title) or @(url),
 *  which is same as http://foo.com.  An anchor ref to an image,
 *  table or section will be @table(name).
 */
	public final void mLINK(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LINK;
		int _saveIndex;
		
		String url=null, title=null;
		
		
		if (!(getLine()!=1))
		  throw new SemanticException("getLine()!=1");
		captureText();
		{
		switch ( LA(1)) {
		case '@':
		{
			_saveIndex=text.length();
			match("@(");
			text.setLength(_saveIndex);
			{
			int _cnt14=0;
			_loop14:
			do {
				if ((_tokenSet_0.member(LA(1)))) {
					{
					match(_tokenSet_0);
					}
				}
				else {
					if ( _cnt14>=1 ) { break _loop14; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				
				_cnt14++;
			} while (true);
			}
			url=new String(text.getBuffer(),_begin,text.length()-_begin); text.setLength(_begin); text.append("");
			{
			switch ( LA(1)) {
			case ',':
			{
				_saveIndex=text.length();
				match(',');
				text.setLength(_saveIndex);
				{
				int _cnt18=0;
				_loop18:
				do {
					if ((_tokenSet_8.member(LA(1)))) {
						{
						match(_tokenSet_8);
						}
					}
					else {
						if ( _cnt18>=1 ) { break _loop18; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
					}
					
					_cnt18++;
				} while (true);
				}
				title=new String(text.getBuffer(),_begin,text.length()-_begin);
				break;
			}
			case ')':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			_saveIndex=text.length();
			match(')');
			text.setLength(_saveIndex);
			translator.link(url,title);
			break;
		}
		case 'h':
		{
			match("http://");
			{
			int _cnt21=0;
			_loop21:
			do {
				if ((_tokenSet_9.member(LA(1)))) {
					{
					match(_tokenSet_9);
					}
				}
				else {
					if ( _cnt21>=1 ) { break _loop21; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				
				_cnt21++;
			} while (true);
			}
			translator.link(new String(text.getBuffer(),_begin,text.length()-_begin),null);
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
/** First line of outermost file (stdin usually) is title */
	public final void mTITLE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = TITLE;
		int _saveIndex;
		
		if (!(getLine()==1&&getColumn()==1&&!context.isNestedTranslator()))
		  throw new SemanticException("getLine()==1&&getColumn()==1&&!context.isNestedTranslator()");
		{
		_loop24:
		do {
			if ((LA(1)==' ') && (_tokenSet_3.member(LA(2))) && (true) && (true) && (true)) {
				_saveIndex=text.length();
				match(' ');
				text.setLength(_saveIndex);
			}
			else if ((LA(1)=='\t') && (_tokenSet_3.member(LA(2))) && (true) && (true) && (true)) {
				_saveIndex=text.length();
				match('\t');
				text.setLength(_saveIndex);
			}
			else {
				break _loop24;
			}
			
		} while (true);
		}
		{
		int _cnt26=0;
		_loop26:
		do {
			if ((_tokenSet_3.member(LA(1)))) {
				mNOT_SPECIAL_CHAR(false);
			}
			else {
				if ( _cnt26>=1 ) { break _loop26; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
			}
			
			_cnt26++;
		} while (true);
		}
		title(new String(text.getBuffer(),_begin,text.length()-_begin));
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mNOT_SPECIAL_CHAR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = NOT_SPECIAL_CHAR;
		int _saveIndex;
		
		{
		match(_tokenSet_3);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BR;
		int _saveIndex;
		
		_saveIndex=text.length();
		match('\\');
		text.setLength(_saveIndex);
		_saveIndex=text.length();
		match('\n');
		text.setLength(_saveIndex);
		captureText(); translator.linebreak();
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBLANK(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BLANK;
		int _saveIndex;
		
		_saveIndex=text.length();
		match('\n');
		text.setLength(_saveIndex);
		_saveIndex=text.length();
		match('\\');
		text.setLength(_saveIndex);
		_saveIndex=text.length();
		match('\n');
		text.setLength(_saveIndex);
		captureText(); translator.blankline();
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mPLUGIN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = PLUGIN;
		int _saveIndex;
		Token id=null;
		Token a=null;
		Token out=null;
		Token id2=null;
		Token arg=null;
		
		Vector args=null;
		
		
		{
		if ((LA(1)=='%') && (LA(2)=='d') && (LA(3)=='e') && (LA(4)=='f') && (LA(5)=='(')) {
			_saveIndex=text.length();
			match("%def");
			text.setLength(_saveIndex);
			match('(');
			mID(true);
			id=_returnToken;
			match(',');
			mWS(false);
			mARG(true);
			a=_returnToken;
			match(')');
			define(id.getText(),a.getText());
		}
		else if ((LA(1)=='%') && (LA(2)=='o') && (LA(3)=='u') && (LA(4)=='t') && (LA(5)=='p')) {
			_saveIndex=text.length();
			match("%output");
			text.setLength(_saveIndex);
			_saveIndex=text.length();
			mWS(false);
			text.setLength(_saveIndex);
			mCODE(true);
			out=_returnToken;
			captureText(); translator.verbatim(out.getText());
		}
		else if ((LA(1)=='%') && (_tokenSet_1.member(LA(2))) && (true) && (true) && (true)) {
			match('%');
			mID(true);
			id2=_returnToken;
			{
			if ((LA(1)=='(') && (LA(2)==')')) {
				match('(');
				match(')');
				plugin(id2.getText(), null);
			}
			else if ((LA(1)=='(') && (_tokenSet_0.member(LA(2)))) {
				match('(');
				args=mARGLIST(false);
				match(')');
				plugin(id2.getText(), args);
			}
			else if ((_tokenSet_10.member(LA(1)))) {
				_saveIndex=text.length();
				mWS(false);
				text.setLength(_saveIndex);
				mCODE(true);
				arg=_returnToken;
				Vector v=new Vector();
													  v.addElement(arg.getText());
													  plugin(id2.getText(), v);
													
			}
			else {
				variable(id2.getText());
			}
			
			}
		}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mID(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ID;
		int _saveIndex;
		
		{
		switch ( LA(1)) {
		case 'a':  case 'b':  case 'c':  case 'd':
		case 'e':  case 'f':  case 'g':  case 'h':
		case 'i':  case 'j':  case 'k':  case 'l':
		case 'm':  case 'n':  case 'o':  case 'p':
		case 'q':  case 'r':  case 's':  case 't':
		case 'u':  case 'v':  case 'w':  case 'x':
		case 'y':  case 'z':
		{
			matchRange('a','z');
			break;
		}
		case 'A':  case 'B':  case 'C':  case 'D':
		case 'E':  case 'F':  case 'G':  case 'H':
		case 'I':  case 'J':  case 'K':  case 'L':
		case 'M':  case 'N':  case 'O':  case 'P':
		case 'Q':  case 'R':  case 'S':  case 'T':
		case 'U':  case 'V':  case 'W':  case 'X':
		case 'Y':  case 'Z':
		{
			matchRange('A','Z');
			break;
		}
		case '_':
		{
			match('_');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		{
		_loop48:
		do {
			switch ( LA(1)) {
			case 'a':  case 'b':  case 'c':  case 'd':
			case 'e':  case 'f':  case 'g':  case 'h':
			case 'i':  case 'j':  case 'k':  case 'l':
			case 'm':  case 'n':  case 'o':  case 'p':
			case 'q':  case 'r':  case 's':  case 't':
			case 'u':  case 'v':  case 'w':  case 'x':
			case 'y':  case 'z':
			{
				matchRange('a','z');
				break;
			}
			case 'A':  case 'B':  case 'C':  case 'D':
			case 'E':  case 'F':  case 'G':  case 'H':
			case 'I':  case 'J':  case 'K':  case 'L':
			case 'M':  case 'N':  case 'O':  case 'P':
			case 'Q':  case 'R':  case 'S':  case 'T':
			case 'U':  case 'V':  case 'W':  case 'X':
			case 'Y':  case 'Z':
			{
				matchRange('A','Z');
				break;
			}
			case '_':
			{
				match('_');
				break;
			}
			case '0':  case '1':  case '2':  case '3':
			case '4':  case '5':  case '6':  case '7':
			case '8':  case '9':
			{
				matchRange('0','9');
				break;
			}
			default:
			{
				break _loop48;
			}
			}
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mWS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = WS;
		int _saveIndex;
		
		{
		_loop85:
		do {
			if ((LA(1)==' ') && (_tokenSet_0.member(LA(2))) && ((LA(3) >= '\u0003' && LA(3) <= '\u00ff')) && (true) && (true)) {
				_saveIndex=text.length();
				match(' ');
				text.setLength(_saveIndex);
			}
			else if ((LA(1)=='\t') && (_tokenSet_0.member(LA(2))) && ((LA(3) >= '\u0003' && LA(3) <= '\u00ff')) && (true) && (true)) {
				_saveIndex=text.length();
				match('\t');
				text.setLength(_saveIndex);
			}
			else if ((LA(1)=='\n') && (_tokenSet_0.member(LA(2))) && ((LA(3) >= '\u0003' && LA(3) <= '\u00ff')) && (true) && (true)) {
				_saveIndex=text.length();
				match('\n');
				text.setLength(_saveIndex);
				newline();
			}
			else {
				break _loop85;
			}
			
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mARG(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ARG;
		int _saveIndex;
		Token id=null;
		
		switch ( LA(1)) {
		case '"':
		{
			mSTRING(false);
			break;
		}
		case '%':
		{
			match('%');
			mID(true);
			id=_returnToken;
			String value = (context.getVariable(id.getText())).toString();
			text.setLength(_begin); text.append(value);
			
			break;
		}
		default:
			if ((_tokenSet_11.member(LA(1)))) {
				mTEXT_TIL_END_ARG(false);
			}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mCODE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CODE;
		int _saveIndex;
		
		_saveIndex=text.length();
		match("<<");
		text.setLength(_saveIndex);
		{
		_loop64:
		do {
			// nongreedy exit test
			if ((LA(1)=='>') && (LA(2)=='>') && (true)) break _loop64;
			if ((LA(1)=='\\') && (_tokenSet_2.member(LA(2))) && ((LA(3) >= '\u0003' && LA(3) <= '\u00ff')) && ((LA(4) >= '\u0003' && LA(4) <= '\u00ff')) && (true)) {
				mESC2(false);
			}
			else if ((LA(1)=='\n') && ((LA(2) >= '\u0003' && LA(2) <= '\u00ff')) && ((LA(3) >= '\u0003' && LA(3) <= '\u00ff')) && (true) && (true)) {
				match('\n');
				newline();
			}
			else if (((LA(1) >= '\u0003' && LA(1) <= '\u00ff')) && ((LA(2) >= '\u0003' && LA(2) <= '\u00ff')) && ((LA(3) >= '\u0003' && LA(3) <= '\u00ff')) && (true) && (true)) {
				matchNot(EOF_CHAR);
			}
			else {
				break _loop64;
			}
			
		} while (true);
		}
		_saveIndex=text.length();
		match(">>");
		text.setLength(_saveIndex);
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final Vector  mARGLIST(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Vector args=new Vector();
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ARGLIST;
		int _saveIndex;
		Token a1=null;
		Token a2=null;
		
		mARG(true);
		a1=_returnToken;
		args.addElement(a1.getText());
		{
		_loop36:
		do {
			if ((LA(1)==',')) {
				match(',');
				mWS(false);
				mARG(true);
				a2=_returnToken;
				args.addElement(a2.getText());
			}
			else {
				break _loop36;
			}
			
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
		return args;
	}
	
	protected final void mSTRING(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = STRING;
		int _saveIndex;
		
		_saveIndex=text.length();
		match('"');
		text.setLength(_saveIndex);
		{
		_loop40:
		do {
			if ((_tokenSet_12.member(LA(1)))) {
				matchNot('"');
			}
			else {
				break _loop40;
			}
			
		} while (true);
		}
		_saveIndex=text.length();
		match('"');
		text.setLength(_saveIndex);
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mTEXT_TIL_END_ARG(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = TEXT_TIL_END_ARG;
		int _saveIndex;
		
		{
		int _cnt44=0;
		_loop44:
		do {
			if ((_tokenSet_11.member(LA(1)))) {
				{
				match(_tokenSet_11);
				}
			}
			else {
				if ( _cnt44>=1 ) { break _loop44; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
			}
			
			_cnt44++;
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mESC(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ESC;
		int _saveIndex;
		char  c = '\0';
		
		_saveIndex=text.length();
		match('\\');
		text.setLength(_saveIndex);
		c = LA(1);
		matchNot('\n');
		context.textBetweenMarkup.append(c);
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mESC2(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ESC2;
		int _saveIndex;
		char  c = '\0';
		
		_saveIndex=text.length();
		match('\\');
		text.setLength(_saveIndex);
		c = LA(1);
		matchNot('\n');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLINE_START_THEN_MARKUP(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LINE_START_THEN_MARKUP;
		int _saveIndex;
		
		if (!(getColumn()==1))
		  throw new SemanticException("getColumn()==1");
		mLEFT_EDGE_MARKUP(false);
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mLEFT_EDGE_MARKUP(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LEFT_EDGE_MARKUP;
		int _saveIndex;
		
		int level = TMLContext.LOWEST_LEVEL;
		
		
		{
		switch ( LA(1)) {
		case '<':
		{
			mCODE(false);
			captureText(); translator.code(new String(text.getBuffer(),_begin,text.length()-_begin));
			break;
		}
		case '#':
		{
			mSECTION(false);
			break;
		}
		case '[':
		{
			mTABLE(false);
			inTable=true;
			break;
		}
		case ']':
		{
			mEND_TABLE(false);
			inTable=false;
			break;
		}
		default:
			if ((LA(1)==' ') && (LA(2)==' ') && (LA(3)=='"')) {
				mBLOCKQUOTE(false);
				captureText(); closeAllLists();
				translator.blockquote(new String(text.getBuffer(),_begin,text.length()-_begin));
			}
			else if (((LA(1)==' '||LA(1)=='1'||LA(1)=='o') && (LA(2)==' ') && (true))&&(!inTable)) {
				{
				_loop56:
				do {
					if ((LA(1)==' ')) {
						match("  ");
						level++;
					}
					else {
						break _loop56;
					}
					
				} while (true);
				}
				{
				switch ( LA(1)) {
				case 'o':
				{
					_saveIndex=text.length();
					match("o ");
					text.setLength(_saveIndex);
					captureText(); ul(level);
					break;
				}
				case '1':
				{
					_saveIndex=text.length();
					match("1 ");
					text.setLength(_saveIndex);
					captureText(); ol(level);
					break;
				}
				default:
				{
					throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
				}
				}
				}
			}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBLANK_LINE_THEN_MARKUP(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BLANK_LINE_THEN_MARKUP;
		int _saveIndex;
		
		_saveIndex=text.length();
		match("\n\n");
		text.setLength(_saveIndex);
		context.textBetweenMarkup.append('\n');
		mLEFT_EDGE_MARKUP(false);
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mBLOCKQUOTE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BLOCKQUOTE;
		int _saveIndex;
		
		_saveIndex=text.length();
		match("  \"");
		text.setLength(_saveIndex);
		{
		int _cnt61=0;
		_loop61:
		do {
			if ((LA(1)=='\n')) {
				match('\n');
				newline();
			}
			else if ((_tokenSet_13.member(LA(1)))) {
				{
				match(_tokenSet_13);
				}
			}
			else {
				if ( _cnt61>=1 ) { break _loop61; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
			}
			
			_cnt61++;
		} while (true);
		}
		_saveIndex=text.length();
		match('"');
		text.setLength(_saveIndex);
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mSECTION(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SECTION;
		int _saveIndex;
		
		captureText();
		
		
		if ((LA(1)=='#') && (LA(2)=='#') && (LA(3)=='#') && (LA(4)==' ')) {
			_saveIndex=text.length();
			match("### ");
			text.setLength(_saveIndex);
			{
			int _cnt74=0;
			_loop74:
			do {
				if ((_tokenSet_14.member(LA(1)))) {
					{
					match(_tokenSet_14);
					}
				}
				else {
					if ( _cnt74>=1 ) { break _loop74; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				
				_cnt74++;
			} while (true);
			}
			section(new String(text.getBuffer(),_begin,text.length()-_begin), 2);
		}
		else if ((LA(1)=='#') && (LA(2)=='#') && (LA(3)=='#') && (LA(4)=='#')) {
			_saveIndex=text.length();
			match("#### ");
			text.setLength(_saveIndex);
			{
			int _cnt77=0;
			_loop77:
			do {
				if ((_tokenSet_14.member(LA(1)))) {
					{
					match(_tokenSet_14);
					}
				}
				else {
					if ( _cnt77>=1 ) { break _loop77; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				
				_cnt77++;
			} while (true);
			}
			section(new String(text.getBuffer(),_begin,text.length()-_begin), 1);
		}
		else if ((LA(1)=='#') && (LA(2)=='#') && (LA(3)==' ')) {
			_saveIndex=text.length();
			match("## ");
			text.setLength(_saveIndex);
			{
			int _cnt71=0;
			_loop71:
			do {
				if ((_tokenSet_14.member(LA(1)))) {
					{
					match(_tokenSet_14);
					}
				}
				else {
					if ( _cnt71>=1 ) { break _loop71; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				
				_cnt71++;
			} while (true);
			}
			section(new String(text.getBuffer(),_begin,text.length()-_begin), 3);
		}
		else if ((LA(1)=='#') && (LA(2)==' ')) {
			_saveIndex=text.length();
			match("# ");
			text.setLength(_saveIndex);
			{
			int _cnt68=0;
			_loop68:
			do {
				if ((_tokenSet_14.member(LA(1)))) {
					{
					match(_tokenSet_14);
					}
				}
				else {
					if ( _cnt68>=1 ) { break _loop68; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				
				_cnt68++;
			} while (true);
			}
			section(new String(text.getBuffer(),_begin,text.length()-_begin), 4);
		}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mTABLE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = TABLE;
		int _saveIndex;
		
		_saveIndex=text.length();
		match('[');
		text.setLength(_saveIndex);
		context.inTable=true; captureText(); translator.begin_table();
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mEND_TABLE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = END_TABLE;
		int _saveIndex;
		
		_saveIndex=text.length();
		match(']');
		text.setLength(_saveIndex);
		context.inTable=false; captureText(); translator.end_table();
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCOL_SEP(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = COL_SEP;
		int _saveIndex;
		
		if (!(context.inTable))
		  throw new SemanticException("context.inTable");
		_saveIndex=text.length();
		match('|');
		text.setLength(_saveIndex);
		captureText(); translator.col();
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mROW_SEP(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ROW_SEP;
		int _saveIndex;
		
		if (!(context.inTable))
		  throw new SemanticException("context.inTable");
		match("----");
		captureText(); translator.row();
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
/** The catch-all "else" rule.  Anything not matched as a tag, gets
 *  matched here.  The chars are buffered up until the next tag is
 *  recognized at which point the translator.text() method gets the
 *  text.
 */
	protected final void mTEXT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = TEXT;
		int _saveIndex;
		char  c = '\0';
		
		if ((LA(1)=='\n') && (LA(2)=='\n')) {
			match("\n\n");
			
			newline(); newline();
			captureText();
			closeAllLists();
			translator.paragraph();
			
		}
		else if ((LA(1)=='\n') && (true)) {
			match('\n');
			newline(); context.textBetweenMarkup.append('\n');
		}
		else if ((_tokenSet_2.member(LA(1)))) {
			c = LA(1);
			matchNot('\n');
			context.textBetweenMarkup.append(c);
		}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mWS2(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = WS2;
		int _saveIndex;
		
		{
		int _cnt88=0;
		_loop88:
		do {
			switch ( LA(1)) {
			case ' ':
			{
				_saveIndex=text.length();
				match(' ');
				text.setLength(_saveIndex);
				break;
			}
			case '\t':
			{
				_saveIndex=text.length();
				match('\t');
				text.setLength(_saveIndex);
				break;
			}
			case '\n':
			{
				_saveIndex=text.length();
				match('\n');
				text.setLength(_saveIndex);
				newline();
				break;
			}
			default:
			{
				if ( _cnt88>=1 ) { break _loop88; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
			}
			}
			_cnt88++;
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	
	private static final long[] mk_tokenSet_0() {
		long[] data = new long[8];
		data[0]=-19791209299976L;
		for (int i = 1; i<=3; i++) { data[i]=-1L; }
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 0L, 576460745995190270L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = new long[8];
		data[0]=-1032L;
		for (int i = 1; i<=3; i++) { data[i]=-1L; }
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = new long[8];
		data[0]=-39616778339336L;
		data[1]=-2882303763664601090L;
		for (int i = 2; i<=3; i++) { data[i]=-1L; }
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 1153484493214973952L, 140738159443968L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = new long[8];
		data[0]=-4398046511112L;
		for (int i = 1; i<=3; i++) { data[i]=-1L; }
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = new long[8];
		data[0]=-8L;
		data[1]=-2147483649L;
		for (int i = 2; i<=3; i++) { data[i]=-1L; }
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = new long[8];
		data[0]=-8L;
		data[1]=-2305843009213693953L;
		for (int i = 2; i<=3; i++) { data[i]=-1L; }
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = new long[8];
		data[0]=-2199023255560L;
		for (int i = 1; i<=3; i++) { data[i]=-1L; }
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = new long[8];
		data[0]=-4294968328L;
		for (int i = 1; i<=3; i++) { data[i]=-1L; }
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = { 1152921508901815808L, 0L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = new long[8];
		data[0]=-19945828122632L;
		for (int i = 1; i<=3; i++) { data[i]=-1L; }
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	private static final long[] mk_tokenSet_12() {
		long[] data = new long[8];
		data[0]=-17179869192L;
		for (int i = 1; i<=3; i++) { data[i]=-1L; }
		return data;
	}
	public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
	private static final long[] mk_tokenSet_13() {
		long[] data = new long[8];
		data[0]=-17179870216L;
		for (int i = 1; i<=3; i++) { data[i]=-1L; }
		return data;
	}
	public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());
	private static final long[] mk_tokenSet_14() {
		long[] data = new long[8];
		data[0]=-34359739400L;
		for (int i = 1; i<=3; i++) { data[i]=-1L; }
		return data;
	}
	public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());
	
	}
