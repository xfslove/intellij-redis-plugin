package com.github.xfslove.intellij.plugin.redis.script;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.SyntaxTraverser;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.JBIterable;
import com.intellij.util.containers.JBIterator;

public class CommandIterator<E> extends JBIterator<E> implements ModelIterator<E> {

  final SyntaxTraverser<E> traverser;
  final JBIterator<E> vIt;

  CommandIterator(SyntaxTraverser<E> syntaxTraverser) {
    this.traverser = syntaxTraverser;
    vIt = from(syntaxTraverser.iterator());
  }

  public JBIterable<CommandIterator<E>> cursor() {
    return JBIterator.cursor(this);
  }

  @Override
  public final E object() {
    return this.current();
  }

  @Override
  public final String text() {
    return this.traverser.api.textOf(object()).toString();
  }

  @Override
  public final TextRange range() {
    return this.traverser.api.rangeOf(object());
  }

  @Override
  public final IElementType type() {
    return this.traverser.api.typeOf(object());
  }

  @Override
  public SyntaxTraverser.Api<E> api() {
    return this.traverser.api;
  }

  @Override
  protected E nextImpl() {
    return this.vIt.hasNext() ? this.vIt.next() : this.stop();
  }
}