// This is a generated file. Not intended for manual editing.
package com.github.xfslove.intellij.plugin.redis.lang;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.navigation.ItemPresentation;

public interface RedisCommand extends RedisNamedElement {

  @NotNull
  PsiElement getKey();

  String getName();

  PsiElement setName(String newName);

  PsiElement getNameIdentifier();

  ItemPresentation getPresentation();

}
