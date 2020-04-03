package com.github.xfslove.intellij.plugin.redis.lang.psi;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.github.xfslove.intellij.plugin.redis.lang.psi.RedisTypes.*;

%%

%{
  public _RedisLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _RedisLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

COMMENT="//".*
CRLF=[ \t\n\x0B\f\r]+
COMMAND=(DEL|HGET|HSET|EXISTS|KEYS|SCAN)
FIELD=('([^'\\]|\\.)*'|\"([^\"\\]|\\\"|\\'|\\)*\")
VALUE=('([^'\\]|\\.)*'|\"([^\"\\]|\\\"|\\'|\\)*\")

%%
<YYINITIAL> {
  {WHITE_SPACE}      { return WHITE_SPACE; }


  {COMMENT}          { return COMMENT; }
  {CRLF}             { return CRLF; }
  {COMMAND}          { return COMMAND; }
  {FIELD}            { return FIELD; }
  {VALUE}            { return VALUE; }

}

[^] { return BAD_CHARACTER; }
