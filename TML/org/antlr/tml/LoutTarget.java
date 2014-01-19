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

public class LoutTarget extends NOPTarget implements TMLTranslator {

	protected boolean generateSlides = false;

	public LoutTarget() {
		this(new BufferedWriter(new OutputStreamWriter(System.out)));
	}

	public LoutTarget(String args) {
		this();
		if ( args!=null ) {
			if ( args.equals("slides") ) {
				generateSlides = true;
			}
		}
	}

	public LoutTarget(Writer out) {
		this.out = out;
	}

	public String getTargetLanguage() {
		return "Lout";
	}

	protected String extractLastCommand(String text) {
		if ( text==null ) {
			return null;
		}
		int i = text.lastIndexOf("@");
		// System.out.println("index of @ in ["+text+"]=="+i);
		if ( i<0 ) {
			return null;
		}
		int begin = i;
		i++;
		int end = 0;
		while ( i<text.length() &&
				Character.isLetterOrDigit(text.charAt(i)))
		{
			i++;
		}
		end = i;
		return text.substring(begin, end);
	}

	public void begin() {
		writeln("@SysInclude{tbl}");
		writeln("@SysInclude{diag}");
		if ( generateSlides ) {
			writeln("@SysInclude{slides}");
			writeln("@OverheadTransparencies");
			writeln("@InitialFont {Helvetica Base 16p}");
			//writeln("@PageOrientation {Landscape}");
		}
		else {
			writeln("@SysInclude{doc}");
			writeln("@Doc @Text @Begin");
		}
	}

	/** insert tail.html if found */
	public void end() {
		if ( !generateSlides ) {
			writeln("@End @Text");
		}
		try {
			out.flush();
		}
		catch (IOException ioe) {
			System.err.println("Problem writing Lout output");
			ioe.printStackTrace(System.err);
		}
	}

	public void text(String t) {
		t = t.replaceAll("\"", "\"\\\"\"");
		t = t.replaceAll("/", "\"/\"");
		write(t);
	}

	public void bold(String t) {
		write("@B{" + t + "}");
	}

	public void italic(String t) {
		write("@I{" + t + "}");
	}

	public void tt(String t) {
		write("@F{" + t + "}");
	}

	public void beginListItem(int level) {
		write("@ListItem {");
	}

	public void endListItem(int level) {
		writeln("}");
	}

	public void begin_ul(int level) {
		writeln("@BulletList");
	}

	public void end_ul(int level) {
		writeln("\n@EndList");
	}

	public void begin_ol(int level) {
		writeln("@NumberedList");
	}

	public void end_ol(int level) {
		writeln("\n@EndList");
	}

	public void paragraph() {
		String last = getLastOutputCommand();
		// System.out.println("last cmd=["+last+"]");
		if ( last!=null &&
		     !(last.startsWith("@EndList")||
			  last.startsWith("@QuotedDisplay")) )
		{
			writeln("\n@LP");
		}
	}

	public void linebreak() {
		write(" @LP\n\n");
	}

	public void blankline() {
		write(" @LP @LP\n");
	}

	public void code(String c) {
		write("\n@LP @F @Verbatim @Begin");
		writeln(c);
		write("@End @Verbatim");
	}

	public void verbatim(String rawOutput) {
		write(rawOutput);
	}

	public void blockquote(String q) {
		writeln("@QuotedDisplay { @I {");
		write("``"+q+"''");
		writeln("}}\n");
	}

	public void link(String url, String title) {
		if (title == null) {
            write("@Verbatim{"+url+"}");
            return;
        }
		write("@B{"+title+"} (@Verbatim{"+url+"})");
	}

    public void title(String title) {
		if ( generateSlides ) {
			writeln("@Title{"+title+"}");
			writeln("//");
		}
		else {
			writeln("@Heading{"+title+"}");
		}
    }

	public void beginSectionList(int level) {
		if ( !generateSlides ) {
			writeln("@Begin"+nSubs(level)+"Sections");
		}
	}

	public void endSectionList(int level) {
		if ( !generateSlides ) {
			writeln("@End"+nSubs(level)+"Sections");
		}
	}

	public void beginSection(String title, int level) {
		if ( level==TMLContext.LOWEST_LEVEL && generateSlides ) {
			writeln("@Overhead");
			writeln("  @Title{"+title+"}");
			writeln("  @Format{ @CurveBox @HExpand @VExpand @Body }");
			writeln("@Begin");
		}
		else {
			writeln("@"+nSubs(level)+"Section");
			writeln("  @Title{"+title+"}");
			writeln("@Begin");
		}
	}

	public void endSection(int level) {
		if ( level==TMLContext.LOWEST_LEVEL && generateSlides ) {
			writeln("@End @Overhead");
		}
		else {
			writeln("@End @"+nSubs(level)+"Section");
		}
	}

	public void begin_table() {
		write("\n<TABLE cellspacing=0 border=0>\n");
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

	public boolean isGeneratingSlides() {
		return generateSlides;
	}

	// SUPPORT ROUTINES

	private String nSubs(int n) {
		String s = "";
		for (int i=1; i<n; i++) {
			s += "Sub";
		}
		return s;
	}

}
