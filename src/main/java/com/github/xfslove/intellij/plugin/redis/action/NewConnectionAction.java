package com.github.xfslove.intellij.plugin.redis.action;

import com.github.xfslove.intellij.plugin.redis.storage.Configuration;
import com.github.xfslove.intellij.plugin.redis.ConfigurationDialog;
import com.github.xfslove.intellij.plugin.redis.ExplorerPanel;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * @author wongiven
 * @date created at 2020/3/26
 */
public class NewConnectionAction extends DumbAwareAction {

  private final ExplorerPanel explorerPanel;

  public NewConnectionAction(ExplorerPanel explorerPanel) {
    super("New Connection", "New connection", AllIcons.General.Add);
    this.explorerPanel = explorerPanel;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
    Project project = anActionEvent.getProject();

    Configuration configuration = new Configuration();
    ConfigurationDialog dialog = new ConfigurationDialog(explorerPanel, project, configuration);
    dialog.show();

    if (dialog.isOK()) {
      explorerPanel.newConfiguration(configuration);
    }
  }

}
