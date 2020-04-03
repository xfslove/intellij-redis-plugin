// This is a generated file. Not intended for manual editing.
package com.github.xfslove.intellij.plugin.redis.lang.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;

public interface RedisTypes {

  IElementType PROPERTY = new RedisElementType("PROPERTY");

  IElementType COMMAND = new RedisTokenType("COMMAND");
  IElementType COMMENT = new RedisTokenType("COMMENT");
  IElementType FIELD = new RedisTokenType("FIELD");
  IElementType VALUE = new RedisTokenType("VALUE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == PROPERTY) {
        return new RedisPropertyImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
