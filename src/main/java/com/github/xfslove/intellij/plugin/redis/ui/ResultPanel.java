package com.github.xfslove.intellij.plugin.redis.ui;

import com.github.xfslove.intellij.plugin.redis.action.SelectModuleAction;
import com.github.xfslove.intellij.plugin.redis.script.CommandIterator;
import com.intellij.ide.CommonActionsManager;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Separator;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.EditorFontType;
import com.intellij.openapi.editor.colors.FontPreferences;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * @author wongiven
 * @date created at 2020/4/20
 */
public class ResultPanel extends JPanel {

  private JPanel rootPanel;
  private JPanel toolbarPanel;
  private JPanel resultPanel;

  private JTextArea resultArea;
  private final CommandIterator command;

  public ResultPanel(CommandIterator command) {
    this.command = command;

    initResultPanel();
    initToolbarPanel();

    add(rootPanel);
  }

  private void initResultPanel() {

    resultArea = new JTextArea();
    resultArea.setFont(EditorColorsManager.getInstance().getSchemeForCurrentUITheme().getFont(EditorFontType.BOLD));
    resultArea.setBorder(UIUtil.getTextFieldBorder());
    resultArea.setBackground(UIUtil.getTextFieldBackground());
    resultArea.setEditable(false);
    resultPanel.add(new JBScrollPane(resultArea), BorderLayout.CENTER);

    resultArea.append("> test");
    resultArea.append("\n");
    resultArea.append("> test");
    resultArea.append("\n");
    resultArea.append("> test");

  }

  private void initToolbarPanel() {

    CommonActionsManager actionsManager = CommonActionsManager.getInstance();

    DefaultActionGroup actionGroup = new DefaultActionGroup("ActionsGroup", false);
    actionGroup.add(new SelectModuleAction());
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

  public JTextArea getResultArea() {
    return resultArea;
  }
}
