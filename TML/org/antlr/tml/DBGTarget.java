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

public class DBGTarget extends NOPTarget implements TMLTranslator {

	public String getTargetLanguage() {
		return "DBG";
	}

	/** insert head.html if found */
	public void begin() {
	}

	/** insert tail.html if found */
	public void end() {
	}

	public void text(String t) {
		System.out.println("[<TEXT> " + t + "]");
	}

	public void bold(String t) {
		System.out.println("[<BOLD> " + t + "]");
	}

	public void italic(String t) {
		System.out.println("[<ITALIC> " + t + "]");
	}

	public void tt(String t) {
		System.out.println("[<TT> " + t + "]");
	}

	public void begin_ul(int level) {
		System.out.println("[<UL:" + level + ">]");
	}

	public void begin_ol(int level) {
		System.out.println("[<OL:" + level + ">]");
	}

	public void end_ul(int level) {
		System.out.println("[</UL:" + level + ">]");
	}

	public void end_ol(int level) {
		System.out.println("[</OL:" + level + ">]");
	}

	public void beginListItem(int level) {
		System.out.println("[<LI:" + level + ">]");
	}

	public void paragraph() {
		System.out.println("[<P>]");
	}

	public void linebreak() {
		System.out.println("[<BR>]");
	}

	public void blankline() {
		System.out.println("[<BR><BR>]");
	}

	public void code(String c) {
		System.out.println("[<CODE> " + c + "]");
	}

	public void verbatim(String rawOutput) {
		write(rawOutput);
	}

	public void blockquote(String q) {
		System.out.println("[<BLOCKQUOTE> " + q + "]");
	}

	public void link(String url, String title) {
		System.out.println("[<LINK> " + url + ", " + title + "]");
	}

	public void section(String title, int level) {
		System.out.println("[<SECTION>:" + level + ": " + title + "]");
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
