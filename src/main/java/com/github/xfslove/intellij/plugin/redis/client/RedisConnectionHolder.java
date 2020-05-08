package com.github.xfslove.intellij.plugin.redis.client;

import com.github.xfslove.intellij.plugin.redis.storage.Connection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wongiven
 * @date created at 2020/3/26
 */
public class RedisConnectionHolder {

  private static final Map<Connection, RedisClient> HOLDER = new ConcurrentHashMap<>(16);

  public RedisClient getClient(Connection connection) {
    return HOLDER.putIfAbsent(connection, connection.isCluster() ? new RedisClusterClient(connection) : new RedisClient(connection));
  }

}
