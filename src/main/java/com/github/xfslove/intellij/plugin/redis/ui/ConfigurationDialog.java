package com.github.xfslove.intellij.plugin.redis.ui;

import com.github.xfslove.intellij.plugin.redis.storage.Connection;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * @author wongiven
 * @date created at 2020/3/26
 */
public class ConfigurationDialog extends DialogWrapper {

  private final Connection connection;
  private final Project project;
  private ConfigurationPanel configurationPanel;

  public ConfigurationDialog(Component parent,
                             Project project,
                             Connection connection) {
    super(parent, true);
    this.connection = connection;
    this.project = project;
    init();
    setTitle("New Redis Connection");
  }

  @Nullable
  @Override
  protected JComponent createCenterPanel() {
    configurationPanel = new ConfigurationPanel(project);
    configurationPanel.load(connection);
    return configurationPanel;
  }

  @Override
  protected ValidationInfo doValidate() {
    return configurationPanel.doValidate();
  }

  @Override
  protected void doOKAction() {
    super.doOKAction();
    configurationPanel.apply(connection);
  }
}
