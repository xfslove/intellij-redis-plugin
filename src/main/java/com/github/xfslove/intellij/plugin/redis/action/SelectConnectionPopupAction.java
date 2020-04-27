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
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.ui.EditorNotifications;
import com.intellij.ui.HyperlinkLabel;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wongiven
 * @date created at 2020/4/9
 */
public class SelectConnectionPopupAction extends DumbAwareAction {

  private final HyperlinkLabel configureLabel;

  public SelectConnectionPopupAction(HyperlinkLabel configureLabel) {
    super("Select Connection");
    this.configureLabel = configureLabel;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {

    Project project = e.getProject();
    ConnectionStorage storage = ServiceManager.getService(project, ConnectionStorage.class);
    List<Connection> connectionList = storage.getConnections();

    Map<String, AnAction> actionName2action =
        connectionList.stream().collect(Collectors.toMap(Connection::getName, SelectConnectionAction::new));

    ListPopup runActionsPopup = JBPopupFactory.getInstance().createListPopup(
        new BaseListPopupStep<String>("Connections", new ArrayList<>(actionName2action.keySet())) {
          @Override
          public PopupStep<?> onChosen(String selectedValue, boolean finalChoice) {
            return this.doFinalStep(() -> (actionName2action.get(selectedValue)).actionPerformed(e));
          }
        });

    runActionsPopup.showUnderneathOf(configureLabel);

  }
}
