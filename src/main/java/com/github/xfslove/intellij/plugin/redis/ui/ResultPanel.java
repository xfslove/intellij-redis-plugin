package com.github.xfslove.intellij.plugin.redis.ui;

import com.intellij.ide.CommonActionsManager;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Separator;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.treetable.TreeTable;

import javax.swing.*;
import java.awt.*;

/**
 * @author wongiven
 * @date created at 2020/4/20
 */
public class ResultPanel extends JPanel {

  private JPanel rootPanel;
  private JPanel toolbarPanel;
  private JPanel resultPanel;

  private TreeTable resultTable;

  public ResultPanel() {

    initResultPanel();
    initToolbarPanel();

    add(rootPanel);
  }

  private void initResultPanel() {

    resultPanel.add(new JBScrollPane(resultTable), BorderLayout.CENTER);

  }

  private void initToolbarPanel() {

    CommonActionsManager actionsManager = CommonActionsManager.getInstance();

    DefaultActionGroup actionGroup = new DefaultActionGroup("ActionsGroup", false);
    actionGroup.add(new Separator());

    JComponent actionToolbar = ActionManager.getInstance().createActionToolbar("Actions", actionGroup, true).getComponent();
    toolbarPanel.add(actionToolbar, BorderLayout.CENTER);

  }

  public JPanel getRootPanel() {
    return rootPanel;
  }

  public JPanel getResultPanel() {
    return resultPanel;
  }
}
