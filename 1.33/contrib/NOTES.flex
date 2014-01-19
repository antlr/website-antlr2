From: tmoog@polhode.com (Tom Moog)
17 October 94
Version 0.0

=============================================================================
Caveat Programmer.  This is free software.  There is no warranty or
guarantee that it works.
=============================================================================

This routine provides the glue code necessary to interface flex with
antlr.  This is a minimal implementation.

There are some minor sources of aggravation for the person who wants to
use this.  If it becomes popular its possible that pccts will be changed
to provide the necessary information in a more convenient fashion.

==============================================================================
Steps:
-------------------------------------------------------------------------------
a.  Define the tokens in your .g file using the syntax:

        #token  TokenName

    Do NOT use a regular expression or action.  Antlr will assign TokenName
    a token number in the same way that yacc does.  Use grep to extract the
    token symbols from tokens.h for inclusion in the flex program (see
    item (d) below).

b.  Do NOT use inline regular expressions in your Antlr grammar.  An inline
    regular expression implicitly creates a #token statement with the
    associated regular expression.

c.  When -k or -ck is greater than 1 then LL_K is defined in parser.dlg
    rather than a header file:

        > grep LL_K parser.dlg >LL_K.h

    This file will be empty if k=1 and ck=1.

d.  tokens.h includes all sorts of information besides the token values.
    If the file is #included it will generate references to DLG related
    items:

        > grep define tokens.h >tokens_only.h

e.  ZZLEXBUFSIZE is defined in pccts/anltr.h, but to #include the full
    file will references to DLG related items:

        > grep ZZLEXBUFSIZE pccts/h/antlr.h >ZZLEXBUFSIZE.h

    I believe the value is 4000 for pccts version 1.30 and 2000 for pccts
    version 1.23.

f.  NLA is in antlr.h with many unrelated items.  It is more convenient
    to set it by calling a routine which is defined in the .g file.

    #include "setNLA.h"

        +-------------------------------------------------+
        | void setNLA (int token) {NLA=token;return}      |
        +-------------------------------------------------+

===============================================================================
How it works:
-------------------------------------------------------------------------------
a.  It emulates the DLG routine zzrdstream (FILE * f) by copying "f" to "yyin".
b.  It emulates the DLG routine zzgettok() by calling yylex().
c.  It copies yytext to zzlextext on return from yylex().
d.  If the yytext string is larger than zzbufsize then it set zzbufovf
    to 1.
e.  It translates the flex end-of-file code (zero) by setting the DLG
    NLA variable to the DLG end-of-file code of 1.

===============================================================================
What it doesn't do:
-------------------------------------------------------------------------------
a.  At the moment it won't emulate the DLG #lexclass code zzmode() and
    zzauto.  Once it appears to work this can be emulated using flex BEGIN
    and yystart.

b.  It doesn't provide any of the #token action variables or routines.
    Many of them have no flex equivalent or the equivalent requires no
    translation.

        1. flex does not track column numbers or line numbers
           Create your own int variables of the same name
           as the DLG equivalent containing this information.

        2. To ignore input text, as with zzskip(), omit the flex action.

        3. To append to a token, as with zzmore(), use yymore().
<FF>
===============================================================================
Glue routine: dlgflex.c
-------------------------------------------------------------------------------
/*
  dlgflex.c
  Provides glue between Antlr and flex generated lexical analyzer
  version 0.1 of 17-Oct-94
  Caveat Programmer
*/

#include "config.h"
#include <stdio.h>

/*  Obtained by: grep LL_K scan.c >LLK.h        */

#include "LL_K.h"

/*  Obtained from: pccts/antlr.h                */

#include "ZZLEXBUFSIZE.h"

int     zzbufsize;
int     zzbufovf;
char *  zzlextext;
int     zzlabase=0;
int     zzlap=0;
int     zzbegcol=0;
int     zzendcol=0;
int     zzline=0;

#ifdef LL_K
  int   zztokenLA[LL_K];
  char  zztextLA[LL_K][ZZLEXBUFSIZE];
#else
  int   zztoken;
#endif

extern char *   yytext;
extern FILE *   yyin;

/******************************************************************************/
#ifdef __USE_PROTOS
   extern int yylex(void);
#else
   extern int yylex();
#endif



/******************************************************************************/
#ifdef __USE_PROTOS
   extern void setNLA(int);
#else
   extern void setNLA();
#endif
/******************************************************************************/
#ifdef __USE_PROTOS
void zzrdstream(FILE *f)
#else
void zzrdstream(f)
   FILE *       f;
#endif
{
   if (f) {
      yyin=f;
   };
   return;
}
/******************************************************************************/
#ifdef __USE_PROTOS
void zzgettok(void)
#else
void zzgettok()
#endif
{
   char *       src;
   char *       dst;
   char *       endDst;
   int          result;

   result=yylex();
   if (result == 0) {
      setNLA(1);
   } else {
      setNLA(result);

      for (src=yytext, dst=zzlextext, endDst=&zzlextext[zzbufsize-1];
           *src != 0 && dst < endDst;
           src++,dst++) {
       *dst=*src;
      };
      if (*src != 0) {
         zzbufovf=1;
      } else {
         zzbufovf=0;
      };
      *dst=0;
   };
   return;
}

===============================================================================
flex program: fl.l
-------------------------------------------------------------------------------
%{
/* Demonstrate extremely simple interface between flex and Antlr
   Version 0.0 of 13-Oct-94
   Caveat Programmer
*/

/* Created by: grep define tokens.h >tokens_only.h      */

#include "tokens_only.h"

%}

%%

[ \t]*          /* ignore */
[a-zA-Z]*       {return (ID);}
[0-9]*          {return (Num);}
\n              {return (NL);}

===============================================================================
pccts grammar file: fldemo.g

  Note use of #token statements with neither regular expressions nor actions
-------------------------------------------------------------------------------
#header <<

#include "charbuf.h"

>>

<<
int     AntlrCount=0;

void setNLA(int token) {
        NLA=token;return;
}

int main() {
again:  ANTLR (statement(),stdin);
        return 0;
}

#define LANL(i) (*LATEXT(i) == '\n' ? "NL" : LATEXT(i))

void laDump(char * label) {
  AntlrCount++;
  printf ("\tRecognized: %s (AntlrCount=%d)\n",label,AntlrCount);
  printf ("\tzzlextext=%s\n",zzlextext);
  return;
}

>>

#token  ID
#token  Num
#token  NL


statement : ( format1 | format2 | format3 ) + "@"

;format1 :
        ID <<laDump("-> Format 1: ID");>>       NL
;format2 :
        Num <<laDump("-> Format 2: Num");>>     NL
;format3 :
        NL  <<laDump("-> Format 4: NL");>>
;
-------------------------------------------------------------------------------
