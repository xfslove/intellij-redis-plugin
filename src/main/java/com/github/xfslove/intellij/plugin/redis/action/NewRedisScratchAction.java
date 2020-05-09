package com.github.xfslove.intellij.plugin.redis.action;

import com.github.xfslove.intellij.plugin.redis.lang.RedisFileType;
import com.github.xfslove.intellij.plugin.redis.ui.ExplorerPanel;
import com.intellij.icons.AllIcons;
import com.intellij.ide.scratch.ScratchFileService;
import com.intellij.ide.scratch.ScratchRootType;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.PathUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author wongiven
 * @date created at 2020/3/27
 */
public class NewRedisScratchAction extends DumbAwareAction {

  private final ExplorerPanel explorerPanel;

  public NewRedisScratchAction(ExplorerPanel explorerPanel) {
    super("New Command Editor", "New command editor", AllIcons.Actions.Execute);
    this.explorerPanel = explorerPanel;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {

    Project project = e.getProject();
    String fileName = PathUtil.makeFileName("redis-scratch", RedisFileType.INSTANCE.getDefaultExtension());
    ScratchFileService fileService = ScratchFileService.getInstance();

    try {

      VirtualFile file = fileService.findFile(ScratchRootType.getInstance(), fileName, ScratchFileService.Option.create_new_always);
      file.putUserData(ExplorerPanel.SELECTED_CONFIG, explorerPanel.getSelectedConfiguration());
      FileEditorManager.getInstance(project).openFile(file, true);
    } catch (IOException ex) {

      Notifications.Bus.notify(
          new Notification(
              "intellij-redis-plugin",
              "",
              ExceptionUtils.getStackTrace(ex),
              NotificationType.ERROR)
      );

    }

  }

  @Override
  public void update(@NotNull AnActionEvent e) {

    e.getPresentation().setVisible(explorerPanel.getSelectedConfiguration() != null);

  }

}
