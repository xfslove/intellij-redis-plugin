package com.github.xfslove.intellij.plugin.redis.experimental.script;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.SyntaxTraverser;
import com.intellij.psi.tree.IElementType;

public interface ModelIterator<E> {

  String text();

  TextRange range();

  IElementType type();

  SyntaxTraverser.Api<E> api();

  long rangeOffset();

  E object();
}