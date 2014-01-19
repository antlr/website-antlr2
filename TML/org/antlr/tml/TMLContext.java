/*
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
package org.antlr.tml;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Stack;

/** Track everything about a translation in progress.  Translation targets
 *  get this context.  Plugins generate text that must be interpreted as TML.
 *  We create a new lexer to handle the string, but the new lexer must share
 *  context with the original (enclosing) lexer.  This object is what is
 *  shared among lexers.
 */
public class TMLContext {

	/** Lowest section/overhead level etc... */
	public static int LOWEST_LEVEL = 1;

	/** Indication that we are below the lowest level and not in a section */
	public static int NOT_IN_SECTION_LEVEL = LOWEST_LEVEL-1;

	/** List of package(s) to search for plugin names.  CLASSPATH
	 *  must include dirs containing package roots listed here.
	 */
	public static String PLUGINS = "plugins";

	protected boolean inTable = false;
    protected Stack listTypeStack = new Stack();
	protected int sectionLevel = NOT_IN_SECTION_LEVEL;
    protected StringBuffer textBetweenMarkup = new StringBuffer(1000);

	protected Stack inputNameStack = new Stack();

	protected TMLLexer lexer = null;

	/** Tracks a list of Class objects */
	protected Hashtable pluginCache = new Hashtable();

	protected Hashtable variables = new Hashtable();

	public TMLContext(TMLLexer lexer) {
		// predefined variables
		Vector p = new Vector();
		p.addElement("org.antlr.tml.plugin");
		variables.put(PLUGINS, p);
		this.lexer = lexer;
	}

	public boolean isNestedTranslator() {
		return inputNameStack.size()>1;
	}

	/** The plugins will want to know what the output target language is */
	public TMLTranslator getTranslator() {
		return lexer.getTranslator();
	}

	public void pushInputName(String name) {
		inputNameStack.push(name);
	}

	public String popInputName() {
		return (String)inputNameStack.pop();
	}

	public String getInputName() {
		return (String)inputNameStack.peek();
	}

	/** What line number in the file are we at? */
	public int getLine() {
		return lexer.getLine();
	}

	/** What column number in the file are we at? */
	public int getColumn() {
		return lexer.getColumn();
	}

	public Object getVariable(String id) {
		return variables.get(id);
	}

	public void defineVariable(String id, Object value) {
		if (getVariable(id) != null) {
			variables.remove(id);
		}
		variables.put(id, value);
	}

	/** Get from cache or using predefined variable PLUGINS */
	public Class getPlugin(String name) {
		// System.out.println(getVariable(PLUGINS) + "." + name);
		Vector plugins = (Vector) getVariable(PLUGINS);
		for (int i = 0; i < plugins.size(); i++) {
			String pkg = (String) plugins.elementAt(i);
			String className = pkg + "." + name;
			try {
				Class c = Class.forName(className);
				pluginCache.put(name, c);
				return c;
			}
			catch (ClassNotFoundException cnf) {
				System.err.println("Unknown plugin " + name + "; ignoring...");
			}
		}
		System.err.println("Unknown plugin " + name + "; ignoring...");
		return null;
	}
}
