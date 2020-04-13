package com.github.xfslove.intellij.plugin.redis.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

/**
 * @author wongiven
 * @date created at 2020/4/8
 */
public class ExecCommandsAction extends DumbAwareAction {

  public ExecCommandsAction() {
    super("Run Commands");
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {


  }
}
