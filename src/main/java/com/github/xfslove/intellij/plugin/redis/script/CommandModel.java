package com.github.xfslove.intellij.plugin.redis.script;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.util.containers.JBIterable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandModel extends ScriptModel<PsiElement> {

  private final Script<PsiElement> myScript;

  public CommandModel(@NotNull Editor editor) {
    this(newScriptFor(editor));
  }

  private CommandModel(@NotNull Script<PsiElement> script) {
    this.myScript = script;
  }

  @NotNull
  private static Script<PsiElement> newScriptFor(@NotNull Editor editor) {
    return new TrackingDocScript(editor.getProject(), editor.getDocument());
  }

  @Override
  public CommandModel subModel(@Nullable TextRange range) {
    return new CommandModel(this.myScript.subScript(range));
  }

  @Override
  public JBIterable<CommandIterator> statements() {
    return new CommandIterator(this.myScript.getScript()).cursor();
  }

  @Override
  public TextRange getTextRange() {
    return this.myScript.getRange();
  }

  @Override
  public void dispose() {
    super.dispose();
    if (this.myScript instanceof Disposable) {
      Disposer.dispose((Disposable) this.myScript);
    }
  }

}

