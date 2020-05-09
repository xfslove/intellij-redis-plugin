package com.github.xfslove.intellij.plugin.redis.client;

import com.github.xfslove.intellij.plugin.redis.storage.Connection;
import com.intellij.openapi.util.Disposer;
import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wongiven
 * @date created at 2020/5/8
 */
public class RedisClusterClient extends RedisClient {

  private GenericObjectPool<StatefulRedisClusterConnection<byte[], byte[]>> pool;

  public RedisClusterClient(Connection connection) {
    super(connection);
  }

  @Override
  public boolean test() throws Exception {
    StatefulRedisClusterConnection<byte[], byte[]> c = createLettuceConnection(connection);
    return "PONG".equals(c.sync().ping());
  }

  @Override
  public byte[] get(byte[] key) throws Exception {
    StatefulRedisClusterConnection<byte[], byte[]> c = null;
    try {
      c = getPool().borrowObject();
      return c.sync().get(key);
    } finally {
      if (c != null) { getPool().returnObject(c); }
    }
  }

  private GenericObjectPool<StatefulRedisClusterConnection<byte[], byte[]>> getPool() {
    if (this.pool != null) {
      return this.pool;
    }

    GenericObjectPoolConfig<StatefulRedisClusterConnection<byte[], byte[]>> config = new GenericObjectPoolConfig<>();
    config.setMaxTotal(5);
    config.setMaxIdle(1);
    config.setMinIdle(1);
    GenericObjectPool<StatefulRedisClusterConnection<byte[], byte[]>> pool = ConnectionPoolSupport.createGenericObjectPool(
        () -> createLettuceConnection(connection),
        config
    );
    Disposer.register(Disposer.newDisposable(), this);
    this.pool = pool;
    return this.pool;
  }

  private StatefulRedisClusterConnection<byte[], byte[]> createLettuceConnection(Connection connection) {
    String[] urls = connection.getUrl().split(",");
    String password = connection.retrievePassword();

    List<RedisURI> uriList = Arrays.stream(urls).map(url -> {
      String[] hp = url.split(":");
      RedisURI uri = RedisURI.Builder.redis(hp[0], Integer.parseInt(hp[1])).build();
      if (StringUtils.isNotBlank(password)) {
        uri.setPassword(password);
      }
      return uri;
    }).collect(Collectors.toList());

    return io.lettuce.core.cluster.RedisClusterClient.create(uriList).connect(new ByteArrayCodec());
  }

  @Override
  public void dispose() {
    if (pool != null) {
      pool.close();
    }
  }
}
