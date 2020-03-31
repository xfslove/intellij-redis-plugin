package com.github.xfslove.intellij.plugin.redis.action;

import com.github.xfslove.intellij.plugin.redis.ExplorerPanel;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

/**
 * @author wongiven
 * @date created at 2020/3/30
 */
public class DeleteConnectionAction extends DumbAwareAction {

  private ExplorerPanel explorerPanel;

  public DeleteConnectionAction(ExplorerPanel explorerPanel) {
    super("Delete Connection", "Delete connection", AllIcons.General.Remove);
    this.explorerPanel = explorerPanel;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
    explorerPanel.deleteSelectedConfiguration();
  }
}
