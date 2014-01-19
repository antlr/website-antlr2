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

import java.util.Vector;
import java.io.FileReader;
import java.io.IOException;

public class include implements TMLPlugin {
	public String translate(TMLContext context, Vector args) {
		// get a big string buffer of the contents of the file
		if ( args.size()==0 ) {
			System.err.println("Missing include file name; line "+context.getLine());
			return null;
		}
		String fileName = (String)args.elementAt(0);
		String inputType = "text";
		if ( args.size()==2 ) {
			inputType = (String)args.elementAt(1); // "code" probably
		}
		FileReader fr = null;
		try {
			fr = new FileReader(fileName);
		}
		catch (IOException ioe) {
			System.err.println("Invalid include file name '"+fileName+
							   "'; line "+context.getLine());
			return null;
		}
		StringBuffer sbuf = new StringBuffer(4000);

		try {
			char[] buffer = new char[1024];
			int n;

			while( (n = fr.read(buffer)) > 0) {
				sbuf.append(buffer, 0, n);
			}
			fr.close();
		}
		catch (IOException ioe2) {
			System.err.println("Error reading include file name '"+fileName+
							   "'; line "+context.getLine());
			return null;
		}
		if ( inputType!=null && inputType.equals("code") ) {
			return "<<\n"+sbuf.toString()+">>\n";
		}
		return sbuf.toString();
	}
}
