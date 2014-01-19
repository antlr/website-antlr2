package org.antlr.tml;

import antlr.TokenStreamException;
import antlr.Token;

import java.util.Vector;
import java.io.StringReader;
import java.io.Reader;

public class DefaultTMLEngine extends TMLLexer {
	public DefaultTMLEngine(Reader in, TMLContext context) {
		super(in, context);
	}

	public DefaultTMLEngine(Reader in) {
		super(in);
	}

    /** Begin on-the-fly syntax-directed translation */
    public void translate(TMLTranslator translator)
        throws TokenStreamException
    {
        if ( translator!=null ) {
            this.translator = translator;
        }
        boolean done = false;
        Token t = null;
		int saveSectionLevel = context.sectionLevel;
        if ( !context.isNestedTranslator() ) {
			translator.begin();
		}
        while ( !done ) {
            t=nextToken();
            if ( t.getType() == Token.EOF_TYPE ) {
                done = true;
            }
            else {
                // System.out.println("token=="+t);
            }
        }
        captureText();
        closeAllLists();
        closeSectionsToLevel(saveSectionLevel);
		if ( !context.isNestedTranslator() ) {
			translator.end();
		}
    }

    protected void closeAllLists() {
		closeAllLists(0);
	}

    protected void closeAllLists(int level) {
        while ( context.listTypeStack.size()>level ) {
			translator.endListItem(context.listTypeStack.size());
            closeList();
        }
    }

    protected void closeList() {
        String e = (String)context.listTypeStack.pop();
        if ( e.equalsIgnoreCase("UL") ) {
            int level = context.listTypeStack.size()+1;
            translator.end_ul(level);
        }
        else if ( e.equalsIgnoreCase("OL") ) {
            int level = context.listTypeStack.size()+1;
            translator.end_ol(level);
        }
    }

    protected void captureText() {
        if ( context.textBetweenMarkup.length()>0 ) {
            translator.text(context.textBetweenMarkup.toString());
            context.textBetweenMarkup.setLength(0);
        }
    }

    protected void ul(int level) {
        if ( context.listTypeStack.size()<level ) {
            // starting new list (possibly nested)
            context.listTypeStack.push("UL");
            translator.begin_ul(level);
        }
        else if ( level<context.listTypeStack.size() ) {
            closeAllLists(level);
			translator.endListItem(level);
        }
		else if ( level==context.listTypeStack.size() ) {
			translator.endListItem(level);
		}
        translator.beginListItem(level);
    }

    protected void ol(int level) {
        if ( context.listTypeStack.size()<level ) {
            // starting new list (possibly nested)
            context.listTypeStack.push("OL");
            translator.begin_ol(level);
        }
        else if ( level<context.listTypeStack.size() ) {
            closeAllLists(level);
			translator.endListItem(level);
        }
		else if ( level==context.listTypeStack.size() ) {
			translator.endListItem(level);
		}
        translator.beginListItem(level);
    }

	/** Close levels sectionLevel..level */
	protected void closeSectionsToLevel(int level) {
        // System.out.println("closeSectionsToLevel("+level+")");
		// close all sections
		for (int i=context.sectionLevel; i>level; i--) {
			// System.out.println("i=="+i);
			translator.endSection(i);
			translator.endSectionList(i);
		}
	}

    protected void section(String text, int level) {
        // System.out.println("section("+text+", "+level+") context.Level="+context.sectionLevel);
		closeAllLists();
		if ( level>context.sectionLevel ) {
			translator.beginSectionList(level);
		}
		else if ( level<context.sectionLevel ) {
			closeSectionsToLevel(level);
			translator.endSection(level);
		}
		else if ( level==context.sectionLevel ) {
			translator.endSection(level);
		}
		context.sectionLevel = level; // enter new level
		translator.beginSection(text, level);
    }

    protected void title(String title) {
        translator.title(title);
    }

    protected void define(String id, String value) {
        // System.out.println(id+"="+value);
        context.defineVariable(id, value);
    }

    protected void plugin(String id, Vector args) {
		// System.out.println("Exec plug in "+id+"("+args+")");
        Class c = context.getPlugin(id);
        if ( c!=null ) {
            try {
				// get new instance of the plugin
                TMLPlugin p = (TMLPlugin)c.newInstance();
				// Get output from plugin
                String output = p.translate(context, args);
				if ( output!=null ) {
					// interpret as TML
					translate(context, translator, id, output);
				}
            }
            catch (Exception e) {
                System.err.println("Problems with plugin "+id+": ");
                e.printStackTrace(System.err);
            }
        }
    }

	/** Given a target and current TML context to exec within,
	 *  translate the given text to the appropriate output language.
	 */
	public static void translate(TMLContext context,
								 TMLTranslator target,
								 String inputName,
								 String text)
		throws TokenStreamException // exceptions go back to plugin()
	{
		// System.out.println("Translating output of "+inputName+": ["+text+"]");
		// Create lexer to parse/translate input
		StringReader sr = new StringReader(text);
        DefaultTMLEngine sublexer = new DefaultTMLEngine(sr, context);
        // make sure errors are reported in right file
		context.pushInputName(inputName);
        sublexer.setFilename(inputName);

		// do translation using existing translator for now
		sublexer.translate(target);

		// back to old input name
		context.popInputName();
		sublexer.setFilename(context.getInputName());
		// System.out.println("input back to "+context.getInputName());
	}

    protected void variable(String id) {
        Object value = context.getVariable(id);
        context.textBetweenMarkup.append(value);
        // System.out.println("variable "+id+" (=="+value+")");
    }

    /** Remove the *x* and {x} etc.. from front/back */
    private String trim(String t) {
        if ( t!=null ) {
            return t.substring(1,t.length()-1);
        }
        return null;
    }

    /** Remove the <<x>> from front/back */
    private String trim2(String t) {
        if ( t!=null ) {
            return t.substring(2,t.length()-2);
        }
        return null;
    }

    /*
	public void uponEOF() throws TokenStreamException, CharStreamException {
		System.out.println("uponEOF");
    }
	*/

}
