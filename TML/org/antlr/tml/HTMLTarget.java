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

import java.io.*;

public class HTMLTarget extends NOPTarget implements TMLTranslator {
	public HTMLTarget() {
		this(new BufferedWriter(new OutputStreamWriter(System.out)));
	}

	public HTMLTarget(Writer out) {
		this.out = out;
	}

	public String getTargetLanguage() {
		return "HTML";
	}

	/** insert head.html if found */
	public void begin() {
		writeln("<HTML>");
		writeln("<HEAD>");
		writeln("<body bgcolor=#FFFFFF text=#000000>");
	}

	/** insert tail.html if found */
	public void end() {
		writeln("</BODY>");
		writeln("</HTML>");
		try {
			out.flush();
		}
		catch (IOException ioe) {
			System.err.println("Problem writing HTML output");
			ioe.printStackTrace(System.err);
		}
	}

	protected String extractLastCommand(String text) {
		if ( text==null ) {
			return null;
		}
		int i = text.lastIndexOf("<");
		if ( i<0 ) {
			return null;
		}
		int begin = i;
		i++;
		int end = 0;
		while ( i<text.length() && text.charAt(i)!='>' ) {
			i++;
		}
		if ( i>=text.length() ) {
			return null; // <... not terminated
		}
		end = i;
		return text.substring(begin, end);
	}

	public void text(String t) {
		write(angleBracketsEscape(t));
	}

	public void bold(String t) {
		write("<B>" + angleBracketsEscape(t) + "</B>");
	}

	public void italic(String t) {
		write("<em>" + angleBracketsEscape(t) + "</em>");
	}

	public void tt(String t) {
		write("<TT>" + angleBracketsEscape(t) + "</TT>");
	}

	public void beginListItem(int level) {
		write("<LI>");
	}

	public void endListItem(int level) {
		writeln("</LI>");
	}

	public void begin_ul(int level) {
		write("<UL>\n");
	}

	public void end_ul(int level) {
		write("\n</UL>\n");
	}

	public void begin_ol(int level) {
		write("<OL>\n");
	}

	public void end_ol(int level) {
		write("\n</OL>\n");
	}

	public void paragraph() {
		write("\n<P>");
	}

	public void linebreak() {
		write("<BR>\n");
	}

	public void blankline() {
		write("<BR>\n<BR>\n");
	}

	public void code(String c) {
		write("\n<FONT SIZE=2><PRE>");
		write(angleBracketsEscape(c));
		write("</PRE></FONT>\n\n");
	}

	public void verbatim(String rawOutput) {
		write(rawOutput);
	}

	public void blockquote(String q) {
		write("<BLOCKQUOTE>\n");
		write("\"<i>"+angleBracketsEscape(q)+"</i>\"");
		write("</BLOCKQUOTE>\n\n");
	}

	public void link(String url, String title) {
		if (title == null) {
			title = url;
		}
		title = angleBracketsEscape(title);
		write("<A HREF=\"" + url + "\"><B>" + title + "</B></A>");
	}

	public void title(String title) {
		title = angleBracketsEscape(title);
		writeln("<title>"+title+"</title>");
		writeln("</HEAD>");
		writeln("<BODY>");
		writeln("<H1>"+title+"</H1>");
    }

	public void beginSection(String title, int level) {
		write("<H" + (level+1) + ">");
		title = angleBracketsEscape(title);
		write(title);
		write("</H" + (level+1) + ">\n");
	}

	public void begin_table() {
		write("\n<TABLE cellspacing=0 border=1>\n");
		write("<TR><TD>");
	}

	public void end_table() {
		write("</TD></TR>\n");
		write("</TABLE>\n");
	}

	public void col() {
		write("</TD><TD>");
	}

	public void row() {
		write("</TD>\n</TR>\n<TR>\n<TD>");
	}

	protected String angleBracketsEscape(String q) {
        q = q.replaceAll("<","&lt;");
        q = q.replaceAll(">","&gt;");
		return q;
	}

}
