package com.github.xfslove.intellij.plugin.redis.experimental.script;

import com.github.xfslove.intellij.plugin.redis.lang.RedisLanguage;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.SyntaxTraverser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class SealedDocScript<E> extends DocScript<E> {

  final SealedDocScript<E> parent;
  final CharSequence text;
  final long timeStamp;
  SyntaxTraverser<E> cached;
  SyntaxTraverser<E> raw;

  SealedDocScript(Project project, VirtualFile file, Document document) {
    super(project, file, document, null);
    this.text = document.getImmutableCharSequence();
    this.timeStamp = document.getModificationStamp();
    this.parent = null;
  }

  SealedDocScript(SealedDocScript<E> parent, TextRange range) {
    super(parent.project, parent.vFile, parent.document, range);
    this.text = parent.text;
    this.timeStamp = parent.timeStamp;
    this.parent = parent;
  }

  @NotNull
  @Override
  public SyntaxTraverser<E> getScript() {
    if (this.cached == null) {
      this.cached = this.getScriptImpl();
    }
    return this.cached;
  }

  @Override
  public DocScript<E> subScript(@Nullable TextRange range) {
    SealedDocScript<E> parent = this.parent == null ? this : this.parent;
    return range == null ? parent : new SealedDocScript<>(parent, range);
  }

  @NotNull
  SyntaxTraverser<E> parseRaw() {
    if (this.parent != null) {
      return this.parent.parseRaw();
    }

    if (this.raw == null) {
      this.raw = ScriptModelUtil.parse(this.project, this.text, RedisLanguage.INSTANCE);
    }
    return this.raw;
  }

  @NotNull
  SyntaxTraverser<E> getScriptImpl() {
//    boolean position = this.range instanceof PositionRange;
//    boolean inRange = !position && this.range != null;
    if (this.range != null) {
      return ScriptModelUtil.parse(this.project, this.range.subSequence(this.text), RedisLanguage.INSTANCE);
    }
    return this.parseRaw();
  }
}