package com.github.xfslove.intellij.plugin.redis.lang;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public abstract class RedisNamedElementImpl extends ASTWrapperPsiElement implements RedisNamedElement {

  public RedisNamedElementImpl(@NotNull ASTNode node) {
    super(node);
  }
}