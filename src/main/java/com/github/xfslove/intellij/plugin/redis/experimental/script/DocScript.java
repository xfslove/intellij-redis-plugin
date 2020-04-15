package com.github.xfslove.intellij.plugin.redis.experimental.script;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.UserDataHolder;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class DocScript<V> implements Script<V> {

  final Document document;
  final Project project;
  final VirtualFile vFile;
  final TextRange range;

  DocScript(Project project, VirtualFile vFile, Document document, TextRange range) {
    this.project = project;
    this.vFile = vFile;
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

  @Override
  @NotNull
  public VirtualFile getVirtualFile() {
    return this.vFile;
  }
}