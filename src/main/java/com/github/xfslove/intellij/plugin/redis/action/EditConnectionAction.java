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
 * @date created at 2020/3/30
 */
public class EditConnectionAction extends DumbAwareAction {

  private ExplorerPanel explorerPanel;

  public EditConnectionAction(ExplorerPanel explorerPanel) {
    super("Edit Connection", "Edit connection", AllIcons.General.Settings);
    this.explorerPanel = explorerPanel;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
    Project project = anActionEvent.getProject();

    Configuration configuration = explorerPanel.getSelectedConfiguration();

    if (configuration == null) {
      return;
    }

    Configuration clone = new Configuration();
    clone.setName(configuration.getName());
    clone.setUrl(configuration.getUrl());
    clone.setSavePassword(configuration.isSavePassword());

    ConfigurationDialog dialog = new ConfigurationDialog(explorerPanel, project, clone);
    dialog.show();

    if (dialog.isOK()) {
      explorerPanel.reloadConfiguration(clone);
    }

  }
}
