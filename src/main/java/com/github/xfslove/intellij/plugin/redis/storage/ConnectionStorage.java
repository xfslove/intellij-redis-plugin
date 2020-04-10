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
    name = "intellij-redis-plugin",
    storages = {@Storage("redis-connections.xml")}
)
public class ConnectionStorage implements PersistentStateComponent<ConnectionStorage> {

  private List<Connection> connections = new ArrayList<>();

  @Override
  public ConnectionStorage getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull ConnectionStorage connectionStorage) {
    XmlSerializerUtil.copyBean(connectionStorage, this);
  }

  public void setConnections(List<Connection> connections) {
    this.connections = connections;
  }

  public List<Connection> getConnections() {
    return connections;
  }

}
