package com.github.xfslove.intellij.plugin.redis.action;

import com.github.xfslove.intellij.plugin.redis.ExplorerPanel;
import com.github.xfslove.intellij.plugin.redis.lang.psi.RedisFile;
import com.github.xfslove.intellij.plugin.redis.lang.psi.RedisFileType;
import com.intellij.icons.AllIcons;
import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import org.jetbrains.annotations.NotNull;

/**
 * @author wongiven
 * @date created at 2020/3/27
 */
public class ExecuteCommandAction extends CreateFileFromTemplateAction {

  private final ExplorerPanel explorerPanel;

  public ExecuteCommandAction(ExplorerPanel explorerPanel) {
    super("Execute Command", "Execute command",  AllIcons.Actions.Execute);
    this.explorerPanel = explorerPanel;
  }

//  @Override
//  public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
//    Project project = anActionEvent.getProject();
//
//    VirtualFile virtualFile = anActionEvent.getData(PlatformDataKeys.VIRTUAL_FILE);
//
////    FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
////    fileEditorManager.openFile(new RedisFile(null).getVirtualFile(), true);
//
//  }

  @Override
  protected void buildDialog(Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder) {
    builder.setTitle("test");
    RedisFileType fileType = RedisFileType.INSTANCE;
    builder.addKind(fileType.getName(), fileType.getIcon(), fileType.getName() + "." + fileType.getDefaultExtension());
  }

  @Override
  public void update(@NotNull AnActionEvent e) {

    e.getPresentation().setVisible(explorerPanel.getSelectedConfiguration() != null);

  }

  @Override
  protected String getActionName(PsiDirectory directory, @NotNull String newName, String templateName) {
    return "test";
  }
}
