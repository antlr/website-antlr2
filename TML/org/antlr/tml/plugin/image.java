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

public class image implements TMLPlugin {
	public String translate(TMLContext context, Vector args) {
		if ( args.size()<1 ) {
			System.err.println("Missing image name/args; line "+context.getLine());
			return null;
		}
		String imageName = (String)args.elementAt(0);
		String width = "";
		String height = "";
		if ( args.size()>1 ) {
			width = (String)args.elementAt(1);
			if ( args.size()>2 ) {
				height = (String)args.elementAt(2);
			}
		}
		String lang = context.getTranslator().getTargetLanguage();
		if ( lang!=null && !lang.equals("HTML") ) {
			// just return file name for anything but HTML for now
			return "\n[Image "+imageName+"]\n";
		}

		// easy in html, just say <img ...>
		//System.out.println("w,h="+width+"," +height);
		if ( width.length()>0 ) {
		    width = "width="+width;
		}
		if ( height.length()>0 ) {
		    height = "height="+height;
		}
		return "%output << <img src=\""+imageName+"\" "+width+" "+height+"> >>";
	}
}
