package com.github.xfslove.intellij.plugin.redis.ui;

import com.github.xfslove.intellij.plugin.redis.action.SelectConnectionAction;
import com.github.xfslove.intellij.plugin.redis.storage.Connection;
import com.intellij.codeInsight.intention.IntentionActionWithOptions;
import com.intellij.icons.AllIcons;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ex.ActionManagerEx;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.EditorNotificationPanel;
import com.intellij.ui.HyperlinkAdapter;
import com.intellij.ui.HyperlinkLabel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.event.HyperlinkEvent;
import java.awt.*;

/**
 * @author wongiven
 * @date created at 2020/4/7
 */
public class CommandEditorPanel extends EditorNotificationPanel {

  private final VirtualFile redisFile;
  private HyperlinkLabel configureLabel;

  public CommandEditorPanel(@NotNull VirtualFile redisFile) {
    this.redisFile = redisFile;
    createConfigureLabels();
  }

  private void createConfigureLabels() {
    Connection selectedConnection = redisFile.getUserData(ExplorerPanel.SELECTED_CONFIG);

    if (selectedConnection != null) {
      return;
    }

    text("You should select one redis connection.").icon(AllIcons.General.NotificationError);

    HyperlinkLabel label = new HyperlinkLabel("Select Connection", getBackground());
    label.addHyperlinkListener(new HyperlinkAdapter() {

      @Override
      protected void hyperlinkActivated(HyperlinkEvent e) {
        CommandEditorPanel.this.executeAction(new SelectConnectionAction());
      }
    });
    label.setToolTipText("Select Connection");
    label.setIcon(AllIcons.General.Settings);
    myLinksPanel.add(label);

    configureLabel = label;
  }


  /**
   *   implement super.executeAction(actionId).
   *   do this to enable create action instead of registry action.
   *   for pass arguments to action.
   */
  protected void executeAction(final AnAction action) {
    final AnActionEvent event = AnActionEvent.createFromAnAction(action, null, getActionPlace(),
        DataManager.getInstance().getDataContext(this));
    action.beforeActionPerformedUpdate(event);
    action.update(event);

    if (event.getPresentation().isEnabled() && event.getPresentation().isVisible()) {
      ActionManagerEx actionManager = ActionManagerEx.getInstanceEx();
      actionManager.fireBeforeActionPerformed(action, event.getDataContext(), event);
      action.actionPerformed(event);
      actionManager.fireAfterActionPerformed(action, event.getDataContext(), event);
    }
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

  public HyperlinkLabel getConfigureLabel() {
    return configureLabel;
  }
}
