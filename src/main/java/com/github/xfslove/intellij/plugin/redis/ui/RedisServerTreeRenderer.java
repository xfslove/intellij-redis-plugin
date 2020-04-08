package com.github.xfslove.intellij.plugin.redis.ui;

import com.github.xfslove.intellij.plugin.redis.RedisIcon;
import com.github.xfslove.intellij.plugin.redis.storage.Configuration;
import com.intellij.ui.ColoredTreeCellRenderer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author wongiven
 * @date created at 2020/3/26
 */
public class RedisServerTreeRenderer extends ColoredTreeCellRenderer {

  @Override
  public void customizeCellRenderer(@NotNull JTree jTree, Object value, boolean isSelected, boolean isExpanded, boolean isLeaf, int row, boolean focus) {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

    Configuration configuration = (Configuration) node.getUserObject();
    if (configuration != null) {
      append(configuration.getName());
      setIcon(RedisIcon.get());
    }
  }


}
