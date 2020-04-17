package com.github.xfslove.intellij.plugin.redis.action;

import com.github.xfslove.intellij.plugin.redis.experimental.DatabaseInEditorResults;
import com.github.xfslove.intellij.plugin.redis.experimental.script.CommandIterator;
import com.github.xfslove.intellij.plugin.redis.experimental.script.CommandModel;
import com.github.xfslove.intellij.plugin.redis.experimental.script.ScriptModelUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class ExecCommandAction extends DumbAwareAction {

  public ExecCommandAction() {
    super("Run Command");
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {

    Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
    VirtualFile virtualFile = e.getRequiredData(CommonDataKeys.VIRTUAL_FILE);
    PsiFile psiFile = e.getRequiredData(CommonDataKeys.PSI_FILE);


    CommandModel<?> commandModel = new CommandModel<>(e.getProject(), virtualFile);
//
//    for (CommandIterator<?> command : commandModel.statements()) {
//      System.out.println(command.text());
//    }
//
//    System.out.println("===================================");
//
    TextRange range = ScriptModelUtil.getSelectionForConsole(editor);

    DatabaseInEditorResults editorResults = new DatabaseInEditorResults();

    DatabaseInEditorResults.Arguments arguments = new DatabaseInEditorResults.Arguments((EditorEx) editor, psiFile, "test", range, commandModel);

    DatabaseInEditorResults.ResultConstructor result = editorResults.getOrCreateResult(arguments);

    result.show();

    System.out.println(result.getOffset());

//    CommandModel<?> subModel = commandModel.subModel(range);
//
//    for (CommandIterator<?> command : subModel.statements()) {
//      System.out.println(command.text());
//    }


  }

}