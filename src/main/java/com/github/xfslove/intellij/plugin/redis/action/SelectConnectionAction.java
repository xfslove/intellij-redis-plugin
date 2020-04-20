package com.github.xfslove.intellij.plugin.redis.action;

import com.github.xfslove.intellij.plugin.redis.storage.Connection;
import com.github.xfslove.intellij.plugin.redis.storage.ConnectionStorage;
import com.github.xfslove.intellij.plugin.redis.ui.ExplorerPanel;
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.codeInsight.documentation.DocumentationManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.ex.EditorEventMulticasterEx;
import com.intellij.openapi.editor.impl.DocumentMarkupModelManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiManager;
import com.intellij.ui.EditorNotifications;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author wongiven
 * @date created at 2020/4/9
 */
public class SelectConnectionAction extends DumbAwareAction {

  public SelectConnectionAction() {
    super("Select Connection");
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {

    Project project = e.getProject();
    // todo show list
    ConnectionStorage storage = ServiceManager.getService(project, ConnectionStorage.class);
    List<Connection> connectionList = storage.getConnections();

    VirtualFile redisFile = e.getRequiredData(CommonDataKeys.VIRTUAL_FILE);
    redisFile.putUserData(ExplorerPanel.SELECTED_CONFIG, connectionList.get(0));

    /*
       notification editor update
       1. editor notification
       2. line marker
       another way:
       DaemonCodeAnalyzer.restart
       EditorNotifications.updateNotifications
     */

    PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(e.getProject());
    psiDocumentManager.reparseFiles(Collections.singleton(redisFile), true);

//    JBPopupFactory.getInstance().createActionGroupPopup()

  }
}
