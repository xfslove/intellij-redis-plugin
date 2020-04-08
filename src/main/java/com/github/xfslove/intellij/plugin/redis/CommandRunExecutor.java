package com.github.xfslove.intellij.plugin.redis;

import com.intellij.execution.ExecutionManager;
import com.intellij.execution.Executor;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.runners.ExecutionEnvironmentBuilder;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class CommandRunExecutor {

  public static final CommandRunExecutor INSTANCE = new CommandRunExecutor();

  public void execute(@NotNull Project project, @NotNull RunnerAndConfigurationSettings runner, @NotNull Executor executor) {
    ExecutionEnvironmentBuilder builder = ExecutionEnvironmentBuilder.createOrNull(executor, runner);
    if (builder != null) {
      ExecutionManager.getInstance(project).restartRunProfile(builder.build());
    }
  }
}
