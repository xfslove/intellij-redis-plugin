// This is a generated file. Not intended for manual editing.
package com.github.xfslove.intellij.plugin.redis.lang.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class RedisVisitor extends PsiElementVisitor {

  public void visitCommand(@NotNull RedisCommand o) {
    visitNamedElement(o);
  }

  public void visitNamedElement(@NotNull RedisNamedElement o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
