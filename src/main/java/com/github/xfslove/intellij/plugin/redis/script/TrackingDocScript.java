package com.github.xfslove.intellij.plugin.redis.script;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.SyntaxTraverser;
import org.jetbrains.annotations.Nullable;

class TrackingDocScript extends DocScript<PsiElement> {

  SealedDocScript sealed;

  TrackingDocScript(Project project, Document document) {
    super(project, document, null);
  }

  @Override
  public DocScript<PsiElement> subScript(@Nullable TextRange range) {
    return this.getSealed().subScript(range);
  }

  @Override
  public SyntaxTraverser<PsiElement> getScript() {
    return this.getSealed().getScript();
  }

  SealedDocScript getSealed() {
    if (this.sealed == null || this.sealed.timeStamp != this.document.getModificationStamp()) {
      this.sealed = new SealedDocScript(this.project, this.document);
    }
    return this.sealed;
  }
}