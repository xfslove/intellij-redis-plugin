package com.github.xfslove.intellij.plugin.redis.client;

import com.github.xfslove.intellij.plugin.redis.storage.Connection;
import com.intellij.openapi.util.Disposer;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author wongiven
 * @date created at 2020/5/8
 */
public class RedisClient {

  protected final Connection connection;

  private final GenericObjectPool<StatefulRedisConnection<byte[], byte[]>> pool;

  public RedisClient(Connection connection) {
    this.connection = connection;
    pool = createPool();
  }

  private GenericObjectPool<StatefulRedisConnection<byte[], byte[]>> createPool() {
    GenericObjectPoolConfig<StatefulRedisConnection<byte[], byte[]>> config = new GenericObjectPoolConfig<>();
    config.setMaxTotal(5);
    config.setMaxIdle(1);
    config.setMinIdle(1);
    GenericObjectPool<StatefulRedisConnection<byte[], byte[]>> pool = ConnectionPoolSupport.createGenericObjectPool(
        () -> {
          String[] hp = connection.getUrl().split(":");
          String password = connection.retrievePassword();

          RedisURI uri = RedisURI.Builder.redis(hp[0], Integer.parseInt(hp[1])).build();
          if (StringUtils.isNotBlank(password)) {
            uri.setPassword(password);
          }
          return io.lettuce.core.RedisClient.create(uri).connect(new ByteArrayCodec());
        },
        config
    );
    Disposer.register(Disposer.newDisposable(), pool::close);
    return pool;
  }

  public byte[] get(byte[] key) throws Exception {
    StatefulRedisConnection<byte[], byte[]> c = null;
    try {
      c = pool.borrowObject();
      return c.sync().get(key);
    } finally {
      if (c != null) { pool.returnObject(c); }
    }
  }

  public boolean test() throws Exception {
    StatefulRedisConnection<byte[], byte[]> c = null;
    try {
      c = pool.borrowObject();
      return "PONG".equals(c.sync().ping());
    } finally {
      if (c != null) { pool.returnObject(c); }
    }
  }
}
