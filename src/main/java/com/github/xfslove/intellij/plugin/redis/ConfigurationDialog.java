package com.github.xfslove.intellij.plugin.redis;

import com.github.xfslove.intellij.plugin.redis.storage.Configuration;
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

  private final Configuration configuration;
  private final Project project;
  private ConfigurationPanel configurationPanel;

  public ConfigurationDialog(Component parent,
                             Project project,
                             Configuration configuration) {
    super(parent, true);
    this.configuration = configuration;
    this.project = project;
    init();
    setTitle("New Redis Connection");
  }

  @Nullable
  @Override
  protected JComponent createCenterPanel() {
    configurationPanel = new ConfigurationPanel(project);
    configurationPanel.load(configuration);
    return configurationPanel;
  }

  @Override
  protected ValidationInfo doValidate() {
    return configurationPanel.doValidate();
  }

  @Override
  protected void doOKAction() {
    super.doOKAction();
    configurationPanel.apply(configuration);
  }
}
