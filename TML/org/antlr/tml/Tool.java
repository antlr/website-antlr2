/*
 [Adapted from BSD licence]
 Copyright (c) 2002-2004 Terence Parr
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

import antlr.*;

import java.io.InputStreamReader;

public class Tool {
	public static String version = "0.1";
	public static void main(String[] args) throws Exception {
		boolean error = false;
		message();
		TMLTranslator target = new HTMLTarget(); // default
		DefaultTMLEngine engine = new DefaultTMLEngine(new InputStreamReader(System.in));
		engine.getContext().pushInputName("<stdin>");
		engine.setFilename("<stdin>");
		if (args != null) {
			int i = 0;
			while (i < args.length && !error) {
				if (args[i].equals("-D")) {
					i++;
					if (i >= args.length) {
						System.err.println("missing assignment for -D");
						error = true;
						break;
					}
					String a = args[i];
					int eq = a.indexOf('=');
					if (eq < 0) {
						System.err.println("missing '=' in assignment for -D");
						error = true;
						break;
					}
					String id = a.substring(0, eq);
					String value = a.substring(eq + 1, a.length());
					System.out.println("override " + id + "=" + value);
					engine.getContext().defineVariable(id, value);
				}
				/*
                else if ( args[i].equals("-lout") ) {
					if ( (i+1)<args.length && args[(i+1)].charAt(0)!='-' ) {
						i++;
						target = new LoutTarget(args[i]);
					}
					else {
						target = new LoutTarget();
					}
				}
                */
				else if ( args[i].equals("-html") ) {
					// default
				}
				/*  TODO: allow random Java class as target
				else if ( args[i].equals("-target") ) {
					i++;
					if (i >= args.length) {
						System.err.println("missing assignment for -target");
						return;
					}
					String a = args[i];
				}
				*/
				else {
					System.err.println("invalid arg: "+args[i]);
					usage();
					error = true;
				}
				i++;
			}
		}

		if ( !error ) {
			try {
				engine.translate(target);
			}
			catch (TokenStreamException atse) {
				System.err.println("TML lexer error: " + atse);
			}
		}
	}

	public static void message() {
		System.err.println("TML Version "+version+" (c) 2002-2004 antlr.org");
	}

	public static void usage() {
		System.err.println("usage: java org.antlr.tml.Tool [args] < file.tml > file.[html|lout]");
		//System.err.println("  -lout [slides]      generate lout Doc output.");
		System.err.println("  -html               generate html output (default).");
		System.err.println("  -D x=y              predefine TML variable x to be y.");
	}

}
