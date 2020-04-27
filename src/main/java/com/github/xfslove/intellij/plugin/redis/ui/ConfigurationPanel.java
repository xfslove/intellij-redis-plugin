package com.github.xfslove.intellij.plugin.redis.ui;

import com.github.xfslove.intellij.plugin.redis.client.RedisClient;
import com.github.xfslove.intellij.plugin.redis.storage.Connection;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.Ref;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.swing.*;

/**
 * @author wongiven
 * @date created at 2020/3/26
 */
public class ConfigurationPanel extends JPanel {

  private JPanel rootPanel;
  private JButton testConnectionButton;
  private JTextField nameField;
  private JTextField urlField;
  private JPasswordField passwordField;
  private JCheckBox savePasswordCheckBox;
  private JCheckBox clusterCheckBox;

  private final Project project;

  public ConfigurationPanel(Project project) {
    this.project = project;

    bindTestConnectionListener();

    add(rootPanel);
  }

  private void bindTestConnectionListener() {
    testConnectionButton.addActionListener(actionEvent -> {
      final Ref<Exception> excRef = new Ref<>();
      final ProgressManager progressManager = ProgressManager.getInstance();
      progressManager.runProcessWithProgressSynchronously(() -> {
        Connection connection = new Connection();
        apply(connection);
        connection.oneTimePassword(getPasswordField());

        final ProgressIndicator progressIndicator = progressManager.getProgressIndicator();
        if (progressIndicator != null) {
          progressIndicator.setText("Connecting to " + connection.getUrl());
        }
        try {
          RedisClient redisClient = ServiceManager.getService(project, RedisClient.class);
          redisClient.connect(connection);
        } catch (Exception ex) {
          excRef.set(ex);
        }
      }, "Testing Connection", true, this.project);

      if (excRef.isNull()) {
        Messages.showInfoMessage(rootPanel, "Connection Test Successful", "Connection Test Successful");
      } else {
        Messages.showErrorDialog(rootPanel, ExceptionUtils.getStackTrace(excRef.get()), "Connection Test Failed");
      }
    });
  }

  private String getNameField() {
    String name = nameField.getText();
    if (StringUtils.isNotBlank(name)) {
      return name;
    }
    return null;
  }

  private void setNameField(String name) {
    if (StringUtils.isNotBlank(name)) {
      nameField.setText(name);
    }
  }

  private String getUrlField() {
    String url = urlField.getText();
    if (StringUtils.isNotBlank(url)) {
      return url;
    }
    return null;
  }

  private void setUrlField(String url) {
    if (StringUtils.isNotBlank(url)) {
      urlField.setText(url);
    }
  }

  private boolean isClusterCheckBoxSelected() {
    return clusterCheckBox.isSelected();
  }

  private void setClusterCheckBoxSelect(boolean select) {
    clusterCheckBox.setSelected(select);
  }

  private boolean isSavePasswordCheckBoxSelected() {
    return savePasswordCheckBox.isSelected();
  }

  private void setSavePasswordCheckBoxSelect(boolean select) {
    savePasswordCheckBox.setSelected(select);
  }

  private String getPasswordField() {
    char[] password = passwordField.getPassword();
    if (password != null && password.length != 0) {
      return String.valueOf(password);
    }
    return null;
  }

  public ValidationInfo doValidate() {

    if (StringUtils.isBlank(getNameField())) {
      return new ValidationInfo("name can not be null");
    }
    String urlField = getUrlField();
    if (StringUtils.isBlank(urlField)) {
      return new ValidationInfo("url can not be null");
    }

    if (isClusterCheckBoxSelected()) {

      String[] nodes = urlField.split(";");
      for (String node : nodes) {
        String[] hp = node.split(":");
        if (hp.length != 2) {
          return new ValidationInfo("url must be host:port1;host:port2;...");
        }
      }

    } else {

      String[] hp = urlField.split(":");
      if (hp.length != 2) {
        return new ValidationInfo("url must be host:port");
      }
    }

    return null;
  }

  public void apply(Connection connection) {
    connection.setName(getNameField());
    connection.setUrl(getUrlField());
    connection.setSavePassword(isSavePasswordCheckBoxSelected());
    connection.setCluster(isClusterCheckBoxSelected());
  }

  public void load(Connection connection) {
    setNameField(connection.getName());
    setUrlField(connection.getUrl());
    setSavePasswordCheckBoxSelect(connection.isSavePassword());
    setClusterCheckBoxSelect(connection.isCluster());
  }
}
