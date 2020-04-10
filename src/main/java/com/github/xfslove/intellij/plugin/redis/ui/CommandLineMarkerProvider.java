package com.github.xfslove.intellij.plugin.redis.ui;

import com.github.xfslove.intellij.plugin.redis.action.ExecCommandAction;
import com.github.xfslove.intellij.plugin.redis.lang.RedisCommand;
import com.github.xfslove.intellij.plugin.redis.lang.RedisTypes;
import com.github.xfslove.intellij.plugin.redis.storage.Connection;
import com.intellij.execution.lineMarker.RunLineMarkerContributor;
import com.intellij.icons.AllIcons;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandLineMarkerProvider extends RunLineMarkerContributor {

  @Nullable
  @Override
  public Info getInfo(@NotNull PsiElement element) {

    if (isRunElement(element)) {

      RedisCommand command = PsiTreeUtil.getParentOfType(element, RedisCommand.class);
      if (command != null) {

//        Project project = element.getProject();
//        PsiFile containingFile = element.getContainingFile();

        VirtualFile virtualFile = element.getContainingFile().getVirtualFile();
        Connection selectedConnection = virtualFile.getUserData(ExplorerPanel.SELECTED_CONFIG);

        if (selectedConnection == null) {
          return null;
        }
        return new Info(AllIcons.RunConfigurations.TestState.Run,
            new AnAction[] {new ExecCommandAction()}, (psiElement) -> "Run Command");
      }
    }

    return null;
  }

  private boolean isRunElement(@NotNull PsiElement element) {
    ASTNode node = element.getNode();

    if (node != null && node.getElementType().equals(RedisTypes.KEY)) {
      return true;
    } else {
      PsiElement parent = element.getParent();
      if (!(parent instanceof RedisCommand)) {
        return false;
      } else {
        RedisCommand command = PsiTreeUtil.getParentOfType(parent, RedisCommand.class);

        return command != null && command.getFirstChild() != null &&
            !command.getFirstChild().getNode().getElementType().equals(RedisTypes.KEY);
      }
    }

  }
}
