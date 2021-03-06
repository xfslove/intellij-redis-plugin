package com.github.xfslove.intellij.plugin.redis.action;

import com.github.xfslove.intellij.plugin.redis.storage.Connection;
import com.github.xfslove.intellij.plugin.redis.storage.ConnectionStorage;
import com.github.xfslove.intellij.plugin.redis.ui.ExplorerPanel;
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.ui.EditorNotifications;
import com.intellij.ui.HyperlinkLabel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wongiven
 * @date created at 2020/4/9
 */
public class SelectConnectionAction extends DumbAwareAction {

  private final Connection hold;

  public SelectConnectionAction(Connection hold) {
    super("Connections");
    this.hold = hold;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    Project project = e.getProject();
    VirtualFile redisFile = e.getRequiredData(CommonDataKeys.VIRTUAL_FILE);
    PsiFile psiFile = e.getRequiredData(CommonDataKeys.PSI_FILE);
    redisFile.putUserData(ExplorerPanel.SELECTED_CONFIG, hold);
     /*
       notification editor update
       1. editor notification
       2. line marker
       another way:
       psiDocumentManager.reparseFiles
     */
    EditorNotifications.getInstance(project).updateNotifications(redisFile);
    DaemonCodeAnalyzer.getInstance(project).restart(psiFile);

  }
}
