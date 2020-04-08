// This is a generated file. Not intended for manual editing.
package com.github.xfslove.intellij.plugin.redis.lang.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;

public interface RedisTypes {

  IElementType COMMAND = new RedisElementType("COMMAND");

  IElementType COMMENT = new RedisTokenType("COMMENT");
  IElementType FIELD = new RedisTokenType("FIELD");
  IElementType KEY = new RedisTokenType("KEY");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == COMMAND) {
        return new RedisCommandImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
