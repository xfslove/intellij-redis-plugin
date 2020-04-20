package com.github.xfslove.intellij.plugin.redis.script;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.UserDataHolder;
import com.intellij.psi.SyntaxTraverser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Script<V> {

  SyntaxTraverser<V> getScript();

  Script<V> subScript(@Nullable TextRange textRange);

  UserDataHolder getUserDataHolder();

  @Nullable
  TextRange getRange();

  @NotNull
  Project getProject();

}
