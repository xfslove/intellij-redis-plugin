package com.github.xfslove.intellij.plugin.redis.action;

import com.github.xfslove.intellij.plugin.redis.experimental.script.CommandIterator;
import com.github.xfslove.intellij.plugin.redis.experimental.script.CommandModel;
import com.github.xfslove.intellij.plugin.redis.experimental.script.ScriptModelUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class ExecCommandAction extends DumbAwareAction {

  public ExecCommandAction() {
    super("Run Command");
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {

    Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
    VirtualFile virtualFile = e.getRequiredData(CommonDataKeys.VIRTUAL_FILE);

    CommandModel<?> commandModel = new CommandModel<>(e.getProject(), virtualFile);

    for (CommandIterator<?> command : commandModel.commands()) {
      System.out.println(command.text());
    }

    System.out.println("===================================");

    TextRange range = ScriptModelUtil.getSelectedCommandRange(commandModel, editor);

    CommandModel<?> subModel = commandModel.subModel(range);

    for (CommandIterator<?> command : subModel.commands()) {
      System.out.println(command.text());
    }


  }

}