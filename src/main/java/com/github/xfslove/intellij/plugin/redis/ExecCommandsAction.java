package com.github.xfslove.intellij.plugin.redis;

import com.intellij.execution.RunManager;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.executors.DefaultRunExecutor;
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
  public void actionPerformed(@NotNull AnActionEvent anActionEvent) {

    CommandRunExecutor executor = CommandRunExecutor.INSTANCE;

    ConfigurationContext context = ConfigurationContext.getFromContext(anActionEvent.getDataContext());

//    RunnerAndConfigurationSettings runner = producer.findExistingConfigurationWithDefaultEnv(context);
    RunManager runManager = RunManager.getInstance(anActionEvent.getProject());

    executor.execute(anActionEvent.getProject(), context.getConfiguration(), DefaultRunExecutor.getRunExecutorInstance());

  }
}
