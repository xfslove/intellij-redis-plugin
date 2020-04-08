package com.github.xfslove.intellij.plugin.redis.action;

import com.github.xfslove.intellij.plugin.redis.lang.RedisFileType;
import com.github.xfslove.intellij.plugin.redis.ui.ExplorerPanel;
import com.intellij.icons.AllIcons;
import com.intellij.ide.scratch.ScratchFileService;
import com.intellij.ide.scratch.ScratchRootType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.PathUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author wongiven
 * @date created at 2020/3/27
 */
public class CommandEditorAction extends DumbAwareAction {

  private final ExplorerPanel explorerPanel;

  public CommandEditorAction(ExplorerPanel explorerPanel) {
    super("Command Editor", "Command editor", AllIcons.Actions.Execute);
    this.explorerPanel = explorerPanel;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
    Project project = anActionEvent.getProject();

    createScratchFile(project, anActionEvent);

  }

  @Override
  public void update(@NotNull AnActionEvent e) {

    e.getPresentation().setVisible(explorerPanel.getSelectedConfiguration() != null);

  }

  private void createScratchFile(@NotNull Project project, @NotNull AnActionEvent event) {

    String fileName = PathUtil.makeFileName("redis-command", RedisFileType.INSTANCE.getDefaultExtension());
    ScratchFileService fileService = ScratchFileService.getInstance();

    VirtualFile file;
    try {
      file = fileService.findFile(ScratchRootType.getInstance(), fileName, ScratchFileService.Option.create_new_always);
    } catch (IOException e) {
      // todo show error
//      showErrorBalloon(project, event, UIBundle.message("create.new.file.could.not.create.file.error.message", new Object[]{fileName}));
      return;
    }


    file.putUserData(ExplorerPanel.ROOT, explorerPanel);

    FileEditor[] fileEditors = FileEditorManager.getInstance(project).openFile(file, true);


//    for (FileEditor curFileEditor : fileEditors) {
//      if (curFileEditor instanceof TextEditor) {
//        VirtualFile curFile = curFileEditor.getFile();
//        if (curFile != null && curFile.getPath().equals(file.getPath())) {
//          TemplateManager.getInstance(project).startTemplate(((TextEditor) curFileEditor).getEditor(), template);
//          return;
//        }
//      }
//    }

//    Messages.showErrorDialog(project, RestClientBundle.message("http.request.add.request.action.error.adding.request.message", new Object[0]), RestClientBundle.message("http.request.add.request.action.error.adding.request", new Object[0]));
  }

}
