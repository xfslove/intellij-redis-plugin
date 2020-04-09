package com.github.xfslove.intellij.plugin.redis.action;

import com.github.xfslove.intellij.plugin.redis.storage.Configuration;
import com.github.xfslove.intellij.plugin.redis.ui.ConfigurationDialog;
import com.github.xfslove.intellij.plugin.redis.ui.ExplorerPanel;
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
  public void actionPerformed(@NotNull AnActionEvent e) {
    Project project = e.getProject();

    Configuration configuration = explorerPanel.getSelectedConfiguration();

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

  @Override
  public void update(@NotNull AnActionEvent e) {

    e.getPresentation().setVisible(explorerPanel.getSelectedConfiguration() != null);

  }
}
