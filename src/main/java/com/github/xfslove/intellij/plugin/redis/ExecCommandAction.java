package com.github.xfslove.intellij.plugin.redis;

import com.github.xfslove.intellij.plugin.redis.storage.Configuration;
import com.github.xfslove.intellij.plugin.redis.ui.ExplorerPanel;
import com.intellij.execution.RunManager;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

public class ExecCommandAction extends DumbAwareAction {

  private final ExplorerPanel explorerPanel;

  public ExecCommandAction(ExplorerPanel explorerPanel) {
    super("Run Command");
    this.explorerPanel = explorerPanel;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent anActionEvent) {

    if (explorerPanel == null) {
      return;
    }

    Configuration selectedConfiguration = explorerPanel.getSelectedConfiguration();

    CommandRunExecutor executor = CommandRunExecutor.INSTANCE;

    ConfigurationContext context = ConfigurationContext.getFromContext(anActionEvent.getDataContext());

//    RunnerAndConfigurationSettings runner = producer.findExistingConfigurationWithDefaultEnv(context);
    RunManager runManager = RunManager.getInstance(anActionEvent.getProject());

    executor.execute(anActionEvent.getProject(), context.getConfiguration(), DefaultRunExecutor.getRunExecutorInstance());



  }

}