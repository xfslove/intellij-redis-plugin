package com.github.xfslove.intellij.plugin.redis.experimental;

import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.UserDataHolder;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.SyntaxTraverser;
import com.intellij.util.containers.JBIterable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Script<V> {

  @NotNull
  Language getLanguage();

  JBIterable<SyntaxTraverser<V>> getScript();

  Script<V> subScript(@Nullable TextRange textRange);

  UserDataHolder getParamDataHolder();

  @Nullable
  TextRange getRange();

  @NotNull
  Project getProject();

  @NotNull
  VirtualFile getVirtualFile();
}
