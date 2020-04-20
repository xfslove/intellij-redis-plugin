package com.github.xfslove.intellij.plugin.redis.script;

import com.github.xfslove.intellij.plugin.redis.lang.RedisTypes;
import com.intellij.openapi.util.Conditions;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.SyntaxTraverser;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.JBIterable;
import com.intellij.util.containers.JBIterator;

public class CommandIterator extends JBIterator<PsiElement> implements ModelIterator<PsiElement> {

  final SyntaxTraverser.Api<PsiElement> api;
  final JBIterator<PsiElement> vIt;

  CommandIterator(SyntaxTraverser<PsiElement> traverser) {
    this.api = traverser.api;
    vIt = from(traverser.iterator());
  }

  CommandIterator(SyntaxTraverser.Api<PsiElement> api, JBIterable<PsiElement> elements) {
    this.api = api;
    vIt = from(elements.iterator());
  }

  public JBIterable<CommandIterator> cursor() {
    return JBIterator.cursor(this);
  }

  @Override
  public String key() {
    PsiElement key = api().children(object()).find(e -> api().typeOf(e).equals(RedisTypes.KEY));
    return key == null ? null : api().textOf(key).toString();
  }

  @Override
  public final PsiElement object() {
    return this.current();
  }

  @Override
  public final String text() {
    return api().textOf(object()).toString();
  }

  @Override
  public final TextRange range() {
    return api().rangeOf(object());
  }

  @Override
  public final IElementType type() {
    return api().typeOf(object());
  }

  @Override
  public SyntaxTraverser.Api<PsiElement> api() {
    return api;
  }

  @Override
  protected PsiElement nextImpl() {
    return vIt.hasNext() ? vIt.next() : this.stop();
  }
}