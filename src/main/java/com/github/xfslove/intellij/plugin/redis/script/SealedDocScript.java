package com.github.xfslove.intellij.plugin.redis.script;

import com.github.xfslove.intellij.plugin.redis.lang.RedisLanguage;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Conditions;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.testFramework.ReadOnlyLightVirtualFile;
import com.intellij.util.containers.TreeTraversal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class SealedDocScript extends DocScript<PsiElement> {

  final SealedDocScript parent;
  final CharSequence text;
  final long timeStamp;
  SyntaxTraverser<PsiElement> cached;
  SyntaxTraverser<PsiElement> raw;

  SealedDocScript(Project project, Document document) {
    super(project, document, null);
    this.text = document.getImmutableCharSequence();
    this.timeStamp = document.getModificationStamp();
    this.parent = null;
  }

  SealedDocScript(SealedDocScript parent, TextRange range) {
    super(parent.project, parent.document, range);
    this.text = parent.text;
    this.timeStamp = parent.timeStamp;
    this.parent = parent;
  }

  @NotNull
  @Override
  public SyntaxTraverser<PsiElement> getScript() {
    if (this.cached == null) {
      this.cached = this.getScriptImpl();
    }
    return this.cached;
  }

  @Override
  public DocScript<PsiElement> subScript(@Nullable TextRange range) {
    SealedDocScript parent = this.parent == null ? this : this.parent;
    return range == null ? parent : new SealedDocScript(parent, range);
  }

  @NotNull
  SyntaxTraverser<PsiElement> parseRaw() {
    if (this.parent != null) {
      return this.parent.parseRaw();
    }

    if (this.raw == null) {
      this.raw = parse(this.project, this.text);
    }
    return this.raw;
  }

  @NotNull
  SyntaxTraverser<PsiElement> getScriptImpl() {
    if (this.range != null) {
      return parse(this.project, this.range.subSequence(this.text));
    }
    return this.parseRaw();
  }

  @NotNull
  private SyntaxTraverser<PsiElement> parse(@NotNull Project project, @NotNull CharSequence documentText) {
    ReadOnlyLightVirtualFile file = new ReadOnlyLightVirtualFile("dummy.redis", RedisLanguage.INSTANCE, documentText);
    SingleRootFileViewProvider.doNotCheckFileSizeLimit(file);
    PsiManager psiManager = PsiManager.getInstance(project);
    SingleRootFileViewProvider viewProvider = new SingleRootFileViewProvider(psiManager, file, false);
    PsiFile psiFile = viewProvider.getPsi(RedisLanguage.INSTANCE);
    return SyntaxTraverser.psiTraverser(psiFile).expand(Conditions.alwaysFalse()).withTraversal(TreeTraversal.LEAVES_DFS);
  }
}