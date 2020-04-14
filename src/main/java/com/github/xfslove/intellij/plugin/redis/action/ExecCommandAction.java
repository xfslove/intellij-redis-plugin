package com.github.xfslove.intellij.plugin.redis.action;

import com.github.xfslove.intellij.plugin.redis.experimental.*;
import com.github.xfslove.intellij.plugin.redis.experimental.RedisEvaluationHelper;
import com.github.xfslove.intellij.plugin.redis.experimental.ScriptModel;
import com.github.xfslove.intellij.plugin.redis.lang.RedisLanguage;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class ExecCommandAction extends DumbAwareAction {

  public ExecCommandAction() {
    super("Run Command");
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {

    RedisEvaluationHelper<Object> helper = new RedisEvaluationHelper<>();

    Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
    PsiFile file = e.getRequiredData(CommonDataKeys.PSI_FILE);
    VirtualFile virtualFile = e.getRequiredData(CommonDataKeys.VIRTUAL_FILE);


    SqlScriptModel sqlScriptModel = new SqlScriptModel<>(e.getProject(), virtualFile, RedisLanguage.INSTANCE);

    for (Object statement : sqlScriptModel.statements()) {

      ScriptModel.StatementIt<PsiElement> it = (ScriptModel.StatementIt<PsiElement>) statement;

      TextRange range = it.range();


    }

  }

}