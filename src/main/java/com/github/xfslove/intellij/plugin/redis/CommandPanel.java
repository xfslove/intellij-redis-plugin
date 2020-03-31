package com.github.xfslove.intellij.plugin.redis;

import com.github.xfslove.intellij.plugin.redis.client.RedisClient;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;

/**
 * @author wongiven
 * @date created at 2020/3/27
 */
public class CommandPanel extends JPanel {

  private JTextArea commandArea;
  private JPanel rootPanel;
  private JPanel toolbarPanel;

  private final Project project;
  private final RedisClient redisClient;

  public CommandPanel(Project project, RedisClient redisClient) {
    this.project = project;
    this.redisClient = redisClient;

    DefaultActionGroup actionGroup = new DefaultActionGroup("ActionGroup", false);
    actionGroup.add(new ExecuteCommandAction(this));

    JComponent actionToolbar = ActionManager.getInstance().createActionToolbar("Actions", actionGroup, true).getComponent();
    toolbarPanel.add(actionToolbar, BorderLayout.CENTER);

    add(rootPanel);
  }


}
