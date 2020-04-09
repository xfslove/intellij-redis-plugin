package com.github.xfslove.intellij.plugin.redis.storage;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wongiven
 * @date created at 2020/3/26
 */
@State(
    name = "Connections",
    storages = {@Storage("redis-connections.xml")}
)
public class ConfigurationStorage implements PersistentStateComponent<ConfigurationStorage> {

  private List<Configuration> configurations = new ArrayList<>();

  @Override
  public ConfigurationStorage getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull ConfigurationStorage configurationStorage) {
    XmlSerializerUtil.copyBean(configurationStorage, this);
  }

  public void setServerConfigurations(List<Configuration> configurations) {
    this.configurations = configurations;
  }

  public List<Configuration> getServerConfigurations() {
    return configurations;
  }

}
