package com.github.xfslove.intellij.plugin.redis.ui;

import com.github.xfslove.intellij.plugin.redis.action.CommandEditorAction;
import com.github.xfslove.intellij.plugin.redis.action.DeleteConnectionAction;
import com.github.xfslove.intellij.plugin.redis.action.EditConnectionAction;
import com.github.xfslove.intellij.plugin.redis.action.NewConnectionAction;
import com.github.xfslove.intellij.plugin.redis.storage.Connection;
import com.github.xfslove.intellij.plugin.redis.storage.ConnectionStorage;
import com.intellij.ide.CommonActionsManager;
import com.intellij.ide.TreeExpander;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Separator;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.tree.TreeUtil;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.util.List;

/**
 * @author wongiven
 * @date created at 2020/3/26
 */
public class ExplorerPanel extends JPanel {

  public static Key<Connection> SELECTED_CONFIG = Key.create("explorerPanel.selectedConfiguration");

  private JPanel rootPanel;
  private JPanel toolbarPanel;
  private JPanel treePanel;

  private Tree redisServersTree;

  private final Project project;

  public ExplorerPanel(Project project) {
    this.project = project;

    initTreePanel();
    initToolbarPanel();

    add(rootPanel);

    ApplicationManager.getApplication().invokeLater(this::initConfigurations);
  }

  private void initTreePanel() {
    redisServersTree = createTree();
    treePanel.add(new JBScrollPane(redisServersTree), BorderLayout.CENTER);
  }

  private void initToolbarPanel() {
    CommonActionsManager actionsManager = CommonActionsManager.getInstance();
    TreeExpander redisServersExpander = createTreeExpander(redisServersTree);

    DefaultActionGroup actionGroup = new DefaultActionGroup("ActionsGroup", false);
    actionGroup.add(new NewConnectionAction(this));
    actionGroup.add(new DeleteConnectionAction(this));
    actionGroup.add(new EditConnectionAction(this));
    actionGroup.add(new Separator());
    actionGroup.add(new CommandEditorAction(this));
    actionGroup.add(new Separator());
    actionGroup.add(actionsManager.createExpandAllAction(redisServersExpander, rootPanel));
    actionGroup.add(actionsManager.createCollapseAllAction(redisServersExpander, rootPanel));

    JComponent actionToolbar = ActionManager.getInstance().createActionToolbar("Actions", actionGroup, true).getComponent();
    toolbarPanel.add(actionToolbar, BorderLayout.CENTER);
  }

  private void initConfigurations() {
    ConnectionStorage storage = ServiceManager.getService(project, ConnectionStorage.class);
    List<Connection> connections = storage.getConnections();
    if (connections.isEmpty()) {
      return;
    }

    // render
    DefaultTreeModel model = (DefaultTreeModel) redisServersTree.getModel();
    DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();
    for (Connection connection : connections) {
      DefaultMutableTreeNode serverNode = new DefaultMutableTreeNode(connection);
      rootNode.add(serverNode);
    }

    TreeUtil.expand(redisServersTree, 1);
  }

  public JPanel getExplorer() {
    return this.rootPanel;
  }

  public void newConfiguration(Connection connection) {
    //save
    connection.storePasswordToOs();
    ConnectionStorage storage = ServiceManager.getService(project, ConnectionStorage.class);
    storage.getConnections().add(connection);

    // render
    DefaultTreeModel model = (DefaultTreeModel) redisServersTree.getModel();
    DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();
    DefaultMutableTreeNode serverNode = new DefaultMutableTreeNode(connection);
    rootNode.add(serverNode);
    model.reload();
  }

  public Connection getSelectedConfiguration() {
    DefaultMutableTreeNode selectedServerNode = (DefaultMutableTreeNode) redisServersTree.getLastSelectedPathComponent();

    if (selectedServerNode == null) {
      return null;
    }

    return  (Connection) selectedServerNode.getUserObject();
  }

  public void deleteSelectedConfiguration() {
    DefaultMutableTreeNode selectedServerNode = (DefaultMutableTreeNode) redisServersTree.getLastSelectedPathComponent();

    if (selectedServerNode == null) {
      return;
    }

    // delete
    Connection selectedConnection = (Connection) selectedServerNode.getUserObject();
    selectedConnection.removePasswordFromOs();
    ConnectionStorage storage = ServiceManager.getService(project, ConnectionStorage.class);
    storage.getConnections().remove(selectedConnection);

    // render
    DefaultTreeModel model = (DefaultTreeModel) redisServersTree.getModel();
    model.removeNodeFromParent(selectedServerNode);
    model.reload();
  }

  public void reloadConfiguration(Connection source) {
    DefaultMutableTreeNode selectedServerNode = (DefaultMutableTreeNode) redisServersTree.getLastSelectedPathComponent();

    if (selectedServerNode == null) {
      return;
    }

    ConnectionStorage storage = ServiceManager.getService(project, ConnectionStorage.class);
    Connection target = (Connection) selectedServerNode.getUserObject();

    if (source.equals(target)) {
      return;
    }

    redisServersTree.setPaintBusy(true);
    redisServersTree.invalidate();

    // update (source -> target)
    target.removePasswordFromOs();
    source.storePasswordToOs();
    int targetIndex = storage.getConnections().indexOf(target);
    storage.getConnections().set(targetIndex, source);

    // render
    selectedServerNode.setUserObject(source);
    ((DefaultTreeModel) redisServersTree.getModel()).reload();

    redisServersTree.revalidate();
    redisServersTree.setPaintBusy(false);
  }

  private TreeExpander createTreeExpander(Tree tree) {
    return new TreeExpander() {
      @Override
      public void expandAll() {
        TreeUtil.expandAll(tree);
      }

      @Override
      public boolean canExpand() {
        return true;
      }

      @Override
      public void collapseAll() {
        TreeUtil.collapseAll(tree, 1);
      }

      @Override
      public boolean canCollapse() {
        return true;
      }
    };
  }

  private Tree createTree() {
    Tree tree = new Tree() {

      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ConnectionStorage storage = ServiceManager.getService(project, ConnectionStorage.class);
        if (storage.getConnections().isEmpty()) {
          treeEmptyNoticeOn(g);
        }
      }
    };

    tree.setName("redisServers");
    tree.setRootVisible(false);
    tree.setCellRenderer(new RedisServerTreeRenderer());
    tree.getEmptyText().clear();
    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

    return tree;
  }

  private void treeEmptyNoticeOn(Graphics g) {
    JLabel myLabel = new JLabel("<html><center>Redis connection list is empty</center></html>");
    myLabel.setFont(getFont());
    myLabel.setBackground(getBackground());
    myLabel.setForeground(getForeground());
    Rectangle bounds = getBounds();
    Dimension size = myLabel.getPreferredSize();
    myLabel.setBounds(0, 0, size.width, size.height);
    int x = (bounds.width - size.width) / 2;
    Graphics g2 = g.create(bounds.x + x, bounds.y + 20, bounds.width, bounds.height);
    try {
      myLabel.paint(g2);
    } finally {
      g2.dispose();
    }
  }

}
