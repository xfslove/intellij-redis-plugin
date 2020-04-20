package com.github.xfslove.intellij.plugin.redis.action;

import com.github.xfslove.intellij.plugin.redis.script.CommandIterator;
import com.github.xfslove.intellij.plugin.redis.script.CommandModel;
import com.github.xfslove.intellij.plugin.redis.ui.InEditorResultUi;
import com.github.xfslove.intellij.plugin.redis.ui.ResultPanel;
import com.intellij.execution.ui.RunnerLayoutUi;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.ui.ComponentWithActions;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.ui.content.Content;
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
    Document document = editor.getDocument();
    int lineNumber = document.getLineNumber(element.getTextOffset());
    int lineStartOffset = document.getLineStartOffset(lineNumber);
    int lineEndOffset = document.getLineEndOffset(lineNumber);

    InEditorResultUi editorResults = ServiceManager.getService(e.getProject(), InEditorResultUi.class);
    InEditorResultUi.Result result = editorResults.getOrCreateResult(editor, lineEndOffset);

    if (result == null) {
      return;
    }

    TextRange textRange = TextRange.create(lineStartOffset, lineEndOffset);
    CommandModel model = new CommandModel(editor);
    CommandModel subModel = model.subModel(textRange);
    CommandIterator<PsiElement> first = subModel.commands().first();

    ResultPanel panel = new ResultPanel();
    RunnerLayoutUi ui = result.getUi();
    Content content = ui.createContent("resultPanel#" + element.getTextOffset(), new ComponentWithActions.Impl(panel.getRootPanel()), "", null, panel.getResultPanel());
    content.setCloseable(true);
    content.setPinnable(true);
    ui.addContent(content);
    result.show();

  }

}