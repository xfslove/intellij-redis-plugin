package com.github.xfslove.intellij.plugin.redis.script;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.UserDataHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class DocScript<V> implements Script<V> {

  final Document document;
  final Project project;
  final TextRange range;

  DocScript(Project project, Document document, TextRange range) {
    this.project = project;
    this.range = range;
    this.document = document;
  }

  @Override
  public abstract DocScript<V> subScript(@Nullable TextRange textRange);

  @Override
  public UserDataHolder getUserDataHolder() {
    return this.document;
  }

  @Override
  @Nullable
  public TextRange getRange() {
    return this.range;
  }

  @Override
  @NotNull
  public Project getProject() {
    return this.project;
  }

}