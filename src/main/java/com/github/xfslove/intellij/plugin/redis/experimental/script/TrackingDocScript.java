package com.github.xfslove.intellij.plugin.redis.experimental.script;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.SyntaxTraverser;
import org.jetbrains.annotations.Nullable;

class TrackingDocScript<E> extends DocScript<E> {

  SealedDocScript<E> sealed;

  TrackingDocScript(Project project, Document document) {
    super(project, document, null);
  }

  @Override
  public DocScript<E> subScript(@Nullable TextRange range) {
    return this.getSealed().subScript(range);
  }

  @Override
  public SyntaxTraverser<E> getScript() {
    return this.getSealed().getScript();
  }

  SealedDocScript<E> getSealed() {
    if (this.sealed == null || this.sealed.timeStamp != this.document.getModificationStamp()) {
      this.sealed = new SealedDocScript<>(this.project, this.document);
    }
    return this.sealed;
  }
}