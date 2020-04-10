package com.github.xfslove.intellij.plugin.redis.action;

import com.github.xfslove.intellij.plugin.redis.CommandRunExecutor;
import com.github.xfslove.intellij.plugin.redis.storage.Connection;
import com.github.xfslove.intellij.plugin.redis.ui.ExplorerPanel;
import com.intellij.execution.RunManager;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

public class ExecCommandAction extends DumbAwareAction {

  public ExecCommandAction() {
    super("Run Command");
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {

    Connection connection = e.getRequiredData(CommonDataKeys.VIRTUAL_FILE).getUserData(ExplorerPanel.SELECTED_CONFIG);

    CommandRunExecutor executor = CommandRunExecutor.INSTANCE;

    ConfigurationContext context = ConfigurationContext.getFromContext(e.getDataContext());

//    RunnerAndConfigurationSettings runner = producer.findExistingConfigurationWithDefaultEnv(context);
    RunManager runManager = RunManager.getInstance(e.getProject());

    executor.execute(e.getProject(), context.getConfiguration(), DefaultRunExecutor.getRunExecutorInstance());



  }

}