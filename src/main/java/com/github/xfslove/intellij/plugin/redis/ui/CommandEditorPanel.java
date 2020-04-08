package com.github.xfslove.intellij.plugin.redis.ui;

import com.github.xfslove.intellij.plugin.redis.ExecCommandsAction;
import com.intellij.codeInsight.intention.IntentionActionWithOptions;
import com.intellij.icons.AllIcons;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.*;
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
public class CommandEditorPanel extends EditorNotificationPanel implements DataProvider {

  public static final DataKey<HyperlinkLabel> EXEC_LABEL = DataKey.create("commandEditorPanel.executeLabel");
  public static final DataKey<HyperlinkLabel> CONFIG_LABEL = DataKey.create("commandEditorPanel.configLabel");

  private final VirtualFile redisFile;
  private final HyperlinkLabel executeLabel;
  private final HyperlinkLabel configLabel;

  public CommandEditorPanel(@NotNull VirtualFile redisFile) {
    this.redisFile = redisFile;
    this.executeLabel = createExecuteLabel();
    this.configLabel = createActionLabel("Configure Format", () -> {
      System.out.println("configure.......");
    });
  }

  private HyperlinkLabel createExecuteLabel() {
    ExplorerPanel rootPanel = redisFile.getUserData(ExplorerPanel.ROOT);

    HyperlinkLabel label = new HyperlinkLabel("Run Commands", getBackground());
    label.addHyperlinkListener(new HyperlinkAdapter() {

      @Override
      protected void hyperlinkActivated(HyperlinkEvent e) {
        CommandEditorPanel.this.executeAction(new ExecCommandsAction(rootPanel));
      }
    });
    label.setToolTipText("Run Commands");
    label.setIcon(AllIcons.Actions.RunAll);
    add("West", label);
    return label;
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

  @Nullable
  @Override
  public Object getData(@NotNull String dataId) {

    if (CommonDataKeys.VIRTUAL_FILE.is(dataId)) {
      return redisFile;
    } else if (EXEC_LABEL.is(dataId)) {
      return executeLabel;
    } else if (CONFIG_LABEL.is(dataId)) {
      return configLabel;
    }
    return null;
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
