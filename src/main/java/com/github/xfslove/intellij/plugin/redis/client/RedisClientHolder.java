package com.github.xfslove.intellij.plugin.redis.client;

import com.github.xfslove.intellij.plugin.redis.storage.Connection;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.util.Disposer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wongiven
 * @date created at 2020/3/26
 */
public class RedisClientHolder {

  private static final Map<Connection, RedisClient> HOLDER = new ConcurrentHashMap<>(16);

  public static RedisClient newClient(Connection connection) {
    return connection.isCluster() ? new RedisClusterClient(connection) : new RedisClient(connection);
  }

  public static void removeClient(Connection connection) {
    RedisClient client = HOLDER.remove(connection);
    client.dispose();
  }

  public static RedisClient getClient(Connection connection) {
    if (!HOLDER.containsKey(connection)) {
      HOLDER.put(connection, newClient(connection));
    }
    return HOLDER.get(connection);
  }

}
