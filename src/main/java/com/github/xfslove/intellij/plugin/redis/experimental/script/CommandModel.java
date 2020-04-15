/*
 * Decompiled with CFR 0.149.
 *
 * Could not load the following classes:
 *  com.intellij.database.Dbms
 *  com.intellij.database.script.ScriptModel
 *  com.intellij.database.script.ScriptModel$ChosenRange
 *  com.intellij.database.script.ScriptModel$ModelItBase
 *  com.intellij.database.script.ScriptModel$PStorage
 *  com.intellij.database.script.ScriptModel$ParamIt
 *  com.intellij.database.script.ScriptModel$PositionRange
 *  com.intellij.database.script.ScriptModel$StatementIt
 *  com.intellij.lang.Language
 *  com.intellij.lang.LanguageUtil
 *  com.intellij.lang.LighterASTNode
 *  com.intellij.openapi.Disposable
 *  com.intellij.openapi.editor.Document
 *  com.intellij.openapi.fileTypes.PlainTextLanguage
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.util.Condition
 *  com.intellij.openapi.util.Conditions
 *  com.intellij.openapi.util.Disposer
 *  com.intellij.openapi.util.TextRange
 *  com.intellij.openapi.util.UserDataHolder
 *  com.intellij.openapi.util.text.StringUtil
 *  com.intellij.openapi.vfs.VirtualFile
 *  com.intellij.psi.PsiElement
 *  com.intellij.psi.PsiFile
 *  com.intellij.psi.SyntaxTraverser
 *  com.intellij.sql.dialects.EvaluationHelper
 *  com.intellij.sql.dialects.SqlLanguageDialect
 *  com.intellij.sql.psi.SqlBatchBlock
 *  com.intellij.sql.psi.SqlParameter
 *  com.intellij.sql.psi.SqlTableType
 *  com.intellij.sql.script.Script
 *  com.intellij.util.Function
 *  com.intellij.util.containers.JBIterable
 *  com.intellij.util.containers.JBIterator
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.xfslove.intellij.plugin.redis.experimental.script;

import com.github.xfslove.intellij.plugin.redis.lang.RedisCommand;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Conditions;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.util.containers.JBIterable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandModel<E> extends ScriptModel<E> {

  private final Script<E> myScript;

  public CommandModel(@NotNull PsiFile file) {
    this(file.getProject(), file.getViewProvider().getVirtualFile());
  }

  public CommandModel(@NotNull Project project, @NotNull VirtualFile virtualFile) {
    this(newScriptFor(project, virtualFile));
  }

  private CommandModel(@NotNull Script<E> script) {
    this.myScript = script;
  }

  @NotNull
  private static <E> Script<E> newScriptFor(@NotNull Project project, @NotNull VirtualFile virtualFile) {
    return new TrackingDocScript<>(project, virtualFile, ScriptModelUtil.getScriptDocument(virtualFile));
  }

  public JBIterable<CommandIterator<E>> commands() {
    return statements()
        .filter(Conditions.compose(ModelIterator::object, Conditions.instanceOf(RedisCommand.class)));
  }

  @Override
  public VirtualFile getVirtualFile() {
    return this.myScript.getVirtualFile();
  }

  @Override
  public CommandModel<E> subModel(@Nullable TextRange range) {
    return new CommandModel<>(this.myScript.subScript(range));
  }

  @Override
  public JBIterable<CommandIterator<E>> statements() {
    return new CommandIterator<>(this.myScript.getScript()).cursor();
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

