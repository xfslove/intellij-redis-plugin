package com.github.xfslove.intellij.plugin.redis;

import com.github.xfslove.intellij.plugin.redis.CommandPanel;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

/**
 * @author wongiven
 * @date created at 2020/3/27
 */
public class ExecuteCommandAction extends DumbAwareAction {

  private final CommandPanel commandPanel;

  public ExecuteCommandAction(CommandPanel commandPanel) {
    super("Execute Command", "Execute command", AllIcons.RunConfigurations.TestState.Run);
    this.commandPanel = commandPanel;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {

  }
}
