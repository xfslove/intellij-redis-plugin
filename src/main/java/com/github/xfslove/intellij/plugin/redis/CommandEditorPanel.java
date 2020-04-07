package com.github.xfslove.intellij.plugin.redis;

import com.intellij.codeInsight.intention.IntentionActionWithOptions;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.EditorNotificationPanel;
import com.intellij.ui.HyperlinkLabel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * @author wongiven
 * @date created at 2020/4/7
 */
public class CommandEditorPanel extends EditorNotificationPanel implements DataProvider {

  private final VirtualFile virtualFile;
  private final HyperlinkLabel executeLabel;

  public CommandEditorPanel(@NotNull VirtualFile virtualFile) {
    this.virtualFile = virtualFile;
    this.executeLabel = createActionLabel("Run command", () -> {});
  }

  @Nullable
  @Override
  public Object getData(@NotNull String dataId) {

    if (CommonDataKeys.VIRTUAL_FILE.is(dataId)) {
      return this.virtualFile;
    } else {
      return executeLabel;
    }

    // todo label

//      if (HttpClientDataKeys.EXAMPLES_TOOLBAR_HYPERLINK_LABEL.is(dataId)) {
//      return this.myExamplesHyperlinkLabel;
//    } else if (HttpClientDataKeys.ADD_REQUEST_TOOLBAR_HYPERLINK_LABEL.is(dataId)) {
//      return this.myAddRequestHyperlinkLabel;
//    } else if (HttpClientDataKeys.RUN_ALL_TOOLBAR_HYPERLINK_LABEL.is(dataId)) {
//      return this.myRunAllRequestsHyperlinkLabel;
//    } else {
//      return HttpClientDataKeys.CREATE_ENV_FILE_HYPERLINK_LABEL.is(dataId) ? this.myCreateEnvFileHyperlinkLabel : null;
//    }
  }

  @Override
  public Color getBackground() {
    return EditorNotificationPanel.getToolbarBackground();
  }

  @Nullable
  @Override
  public IntentionActionWithOptions getIntentionAction() {
    return null;
  }
}
