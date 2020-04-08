package com.github.xfslove.intellij.plugin.redis.ui;

import com.intellij.codeInsight.intention.IntentionActionWithOptions;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.actionSystem.DataProvider;
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

  private final VirtualFile virtualFile;
  private final HyperlinkLabel executeLabel;
  private final HyperlinkLabel configLabel;

  public CommandEditorPanel(@NotNull VirtualFile virtualFile) {
    this.virtualFile = virtualFile;
    this.executeLabel = createExecuteLabel();
//    createActionLabel("Run command", "execCommandAction");
    this.configLabel = createActionLabel("Configure format", () -> {
      System.out.println("configure.......");
    });
  }

  private HyperlinkLabel createExecuteLabel() {
    HyperlinkLabel label = new HyperlinkLabel("Run command", getBackground());
    label.addHyperlinkListener(new HyperlinkAdapter() {

      @Override
      protected void hyperlinkActivated(HyperlinkEvent e) {
        CommandEditorPanel.this.executeAction("execCommandAction");
      }
    });
    label.setToolTipText("Run command");
    label.setIcon(AllIcons.Actions.RunAll);
    add("West", label);
    return label;
  }

  @Nullable
  @Override
  public Object getData(@NotNull String dataId) {

    if (CommonDataKeys.VIRTUAL_FILE.is(dataId)) {
      return virtualFile;
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
