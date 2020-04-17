package com.github.xfslove.intellij.plugin.redis.action;

import com.github.xfslove.intellij.plugin.redis.experimental.DatabaseInEditorResults;
import com.github.xfslove.intellij.plugin.redis.experimental.script.CommandModel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class ExecCommandAction extends DumbAwareAction {

  private final PsiElement element;

  public ExecCommandAction(PsiElement element) {
    super("Run Command");
    this.element = element;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {

    Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
    PsiFile psiFile = e.getRequiredData(CommonDataKeys.PSI_FILE);

    DatabaseInEditorResults editorResults = new DatabaseInEditorResults();

    DatabaseInEditorResults.Arguments arguments = new DatabaseInEditorResults.Arguments(
        "test", (EditorEx) editor, psiFile, element.getTextRange());

    DatabaseInEditorResults.ResultConstructor result = editorResults.getOrCreateResult(arguments);

    result.show();

    System.out.println(result.getOffset());

  }

}