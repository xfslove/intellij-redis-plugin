package com.github.xfslove.intellij.plugin.redis.action;

import com.github.xfslove.intellij.plugin.redis.storage.Configuration;
import com.github.xfslove.intellij.plugin.redis.storage.ConfigurationStorage;
import com.github.xfslove.intellij.plugin.redis.ui.ExplorerPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.EditorNotifications;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author wongiven
 * @date created at 2020/4/9
 */
public class ConfigureServerAction extends DumbAwareAction {

  public ConfigureServerAction() {
    super("Configure Server");
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {

    Project project = e.getProject();
    // todo show list
    ConfigurationStorage storage = ServiceManager.getService(project, ConfigurationStorage.class);
    List<Configuration> configurationList = storage.getServerConfigurations();

    VirtualFile redisFile = e.getRequiredData(CommonDataKeys.VIRTUAL_FILE);
    redisFile.putUserData(ExplorerPanel.SELECTED_CONFIG, configurationList.get(0));

    /*
       notification editor update
       1. editor notification
       2. line marker, todo: this implement maybe has a better way.
     */
    EditorNotifications.getInstance(project).updateNotifications(redisFile);
    Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
    Document document = editor.getDocument();
    WriteCommandAction.runWriteCommandAction(project, () -> document.insertString(0, " "));
    WriteCommandAction.runWriteCommandAction(project, () -> document.deleteString(0, 1));


//    JBPopupFactory.getInstance().createActionGroupPopup()

  }
}
