package com.github.xfslove.intellij.plugin.redis;

import com.github.xfslove.intellij.plugin.redis.client.RedisClient;
import com.github.xfslove.intellij.plugin.redis.storage.Configuration;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.Ref;
import com.intellij.ui.JBColor;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

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
        Configuration configuration = new Configuration();
        apply(configuration);
        configuration.oneTimePassword(getPasswordField());

        final ProgressIndicator progressIndicator = progressManager.getProgressIndicator();
        if (progressIndicator != null) {
          progressIndicator.setText("Connecting to " + configuration.getUrl());
        }
        try {
          RedisClient redisClient = ServiceManager.getService(project, RedisClient.class);
          redisClient.connect(configuration);
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
    if (StringUtils.isBlank(nameField.getText())) {
      return new ValidationInfo("name can not be null");
    }
    if (StringUtils.isBlank(urlField.getText())) {
      return new ValidationInfo("url can not be null");
    }
    return null;
  }

  public void apply(Configuration configuration) {
    configuration.setName(getNameField());
    configuration.setUrl(getUrlField());
    configuration.setSavePassword(isSavePasswordCheckBoxSelected());
  }

  public void load(Configuration configuration) {
    setNameField(configuration.getName());
    setUrlField(configuration.getUrl());
    setSavePasswordCheckBoxSelect(configuration.isSavePassword());
  }
}
