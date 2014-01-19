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
package org.antlr.tml;

import java.io.Writer;
import java.io.IOException;

/** This class represents the NOP translation and can be used to check
 *  syntax of your TML input.  More importantly, it is a useful superclass
 *  for translation targets because when I add features later the targets
 *  by default ignore these enhancements--avoiding the cost of modifying
 *  every target.  The cost is a loss of compile-error when you don't
 *  implement a feature.
 *
 *  Some useful support routines here.  Using write/writeln is good because
 *  it buffers the last output string.  For example, If you need to avoid
 *  generating a paragraph symbol after a list because it makes too much
 *  space, you can look back to see what you just generated.
 */
public class NOPTarget implements TMLTranslator {
	protected Writer out = null;
	protected String lastCommand = null;

	public String getTargetLanguage() {
		return "NOP";
	}

	protected void write(String s) {
		try {
			out.write(s);
			String c = extractLastCommand(s);
			// System.out.println("extracted "+c);
			if ( c!=null ) {
				lastCommand = c;
			}
		}
		catch (IOException ioe) {
			System.err.println("Problem writing output");
			ioe.printStackTrace(System.err);
		}
	}

	protected void writeln(String s) {
		write(s);
		write("\n");
	}

	protected String getLastOutputCommand() {
		return lastCommand;
	}

	/** Subclasses should implement this; i.e., pull out <...> or @ID.
	 *  Groovy because you get a list like this when you from say
	 *  paragraph():
	 *
	 *  last cmd=[@Heading]
	 *  last cmd=[@LP]
	 *  last cmd=[@EndList]
	 *  last cmd=[@Begin]
	 */
	protected String extractLastCommand(String text) {
		return null;
	}

	/** insert head.html if found */
	public void begin() {
	}

	/** insert tail.html if found */
	public void end() {
	}

	public void text(String t) {
	}

	public void bold(String t) {
	}

	public void italic(String t) {
	}

	public void tt(String t) {
	}

	public void begin_ul(int level) {
	}

	public void begin_ol(int level) {
	}

	public void end_ul(int level) {
	}

	public void end_ol(int level) {
	}

	public void beginListItem(int level) {
	}

	public void endListItem(int level) {
	}

	public void paragraph() {
	}

	public void linebreak() {
	}

	public void blankline() {
	}

	public void verbatim(String rawOutput) {
	}

	public void code(String c) {
	}

	public void blockquote(String q) {
	}

	public void link(String url, String title) {
	}

    public void title(String title) {
    }

	public void beginSection(String title, int level) {
	}

	public void endSection(int level) {
	}

	public void beginSectionList(int level) {
	}

	public void endSectionList(int level) {
	}

	public void col() {
	}

	public void row() {
	}

	public void begin_table() {
	}

	public void end_table() {
	}

}
