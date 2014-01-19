James Berry
jberry@teleport.com
Sat Nov 12 13:53:35 EST 1994

Though the PCCts supports a mode stack in standard "C" mode for v1.30, no
such support exists for C++. Here's a recipe for adding such a stack for C++.

Perhaps we can get this support into 1.31???

Thanks to original example by David Seidel.

Add to file DLexerBase.h, class DLGLexerBase:
---------------------------------------------
protected:
#if defined(USER_ZZMODE_STACK)
   int      modeStack[ZZMAXSTK];
   int      modeDepth;
#endif

Replace mode() method declaration in file DLexerBase.h, class DLGLexerBase:
---------------------------------------------------------------------------
public:
#if defined(USER_ZZMODE_STACK)
   virtual void   mode(int k) = 0;  /* switch to automaton 'k' */
   int      getmode(void)  { return automaton; }
   int      swapmode(int newMode);
   void  pushmode(int newMode);
   void  popmode(void);
#else
   void  mode(int k);            /* switch to automaton 'k' */
#endif


Add to file DLexerBase.C
------------------------
#if defined(USER_ZZMODE_STACK)
int DLGLexerBase::
swapmode(int newMode)
{
   int oldMode = getmode();
   mode(newMode);
   return oldMode;
}
#endif


#if defined(USER_ZZMODE_STACK)
void DLGLexerBase::
pushmode(int newMode)
{
   if (modeDepth == ZZMAXSTK - 1)
     panic("Mode stack overflow ");
   else
     modeStack[modeDepth++] = swapmode(newMode);
}
#endif


#if defined(USER_ZZMODE_STACK)
void DLGLexerBase::
popmode(void)
{
   if (modeDepth == 0)
    panic("Mode stack underflow ");
   else
     mode(modeStack[--modeDepth]);
}
#endif

-- 


