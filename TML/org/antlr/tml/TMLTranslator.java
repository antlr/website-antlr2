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

public interface TMLTranslator {
	/** Typically "HTML", or "Lout" */
	public String getTargetLanguage();

	/** Called before text read */
	public void begin();

	/** Called after all text read and translated */
	public void end();

	public void text(String t);

	public void bold(String t);

	public void italic(String t);

	public void tt(String t);

	public void beginListItem(int level);

	public void endListItem(int level);

	public void begin_ul(int level);

	public void begin_ol(int level);

	public void end_ul(int level);

	public void end_ol(int level);

	public void paragraph();

	/** They want a new line in a paragraph */
	public void linebreak();

	/** They want a blank line without signaling a new paragraph.
	 *  Useful for new "paragraphs" within a single bullet.
	 */
	public void blankline();

	public void code(String c);

	/** Assume rawOutput is in output language and just dump */
	public void verbatim(String rawOutput);

	public void blockquote(String q);

	public void link(String url, String title);

	public void title(String title);

	public void beginSection(String title, int level);

	public void endSection(int level);

	public void beginSectionList(int level);

	public void endSectionList(int level);

	public void col();

	public void row();

	public void begin_table();

	public void end_table();
}
