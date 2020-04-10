package com.github.xfslove.intellij.plugin.redis.action;

import com.github.xfslove.intellij.plugin.redis.ui.ExplorerPanel;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

/**
 * @author wongiven
 * @date created at 2020/3/30
 */
public class DeleteConnectionAction extends DumbAwareAction {

  private final ExplorerPanel explorerPanel;

  public DeleteConnectionAction(ExplorerPanel explorerPanel) {
    super("Delete Connection", "Delete connection", AllIcons.General.Remove);
    this.explorerPanel = explorerPanel;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    explorerPanel.deleteSelectedConfiguration();
  }

  @Override
  public void update(@NotNull AnActionEvent e) {

    e.getPresentation().setVisible(explorerPanel.getSelectedConfiguration() != null);

  }
}
