package com.github.xfslove.intellij.plugin.redis;

import com.github.xfslove.intellij.plugin.redis.lang.psi.RedisFileType;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.EditorNotifications;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author wongiven
 * @date created at 2020/4/7
 */
public class CommandEditorPanelProvider extends EditorNotifications.Provider<CommandEditorPanel> {

  public static final Key<CommandEditorPanel> PANEL_KEY = Key.create("CommandEditorPanel");

  @NotNull
  @Override
  public Key<CommandEditorPanel> getKey() {
    return PANEL_KEY;
  }

  @Nullable
  @Override
  public CommandEditorPanel createNotificationPanel(@NotNull VirtualFile file, @NotNull FileEditor fileEditor, @NotNull Project project) {
    return !FileTypeRegistry.getInstance().isFileOfType(file, RedisFileType.INSTANCE) ? null : new CommandEditorPanel(file);
  }
}
