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
package org.antlr.tml.plugin;

import org.antlr.tml.*;
import antlr.TokenStreamException;

import java.util.Vector;
import java.io.StringWriter;

public class box implements TMLPlugin {
	public String translate(TMLContext context, Vector args) {
		if ( args.size()<1 ) {
			System.err.println("Missing TML input; line "+context.getLine());
			return null;
		}
		String tml = (String)args.elementAt(0);
		String lang = context.getTranslator().getTargetLanguage();
		if ( lang==null || !lang.equals("HTML") ) {
            return tml;
		}
		// Use the same context (like what section, line, etc...)
		// but fork a new translator that writes to a string so I can
		// enclose in @Box
		StringWriter output = new StringWriter();
		TMLTranslator target = new HTMLTarget(output);
		try {
			DefaultTMLEngine.translate(context,target,"box",tml);
		}
		catch (TokenStreamException tse) {
			System.err.println("Error parsing box input "+context.getLine());
		}
		return "%output <<\n<table border=1><tr><td>"+output.toString()+"</td></tr></table>\n>>\n";
	}
}
