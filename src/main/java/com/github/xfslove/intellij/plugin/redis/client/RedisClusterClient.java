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

  private final GenericObjectPool<StatefulRedisClusterConnection<byte[], byte[]>> pool;

  public RedisClusterClient(Connection connection) {
    super(connection);
    pool = createPool();
  }

  private GenericObjectPool<StatefulRedisClusterConnection<byte[], byte[]>> createPool() {
    GenericObjectPoolConfig<StatefulRedisClusterConnection<byte[], byte[]>> config = new GenericObjectPoolConfig<>();
    config.setMaxTotal(5);
    config.setMaxIdle(1);
    config.setMinIdle(1);
    GenericObjectPool<StatefulRedisClusterConnection<byte[], byte[]>> pool = ConnectionPoolSupport.createGenericObjectPool(
        () -> {
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
        },
        config
    );
    Disposer.register(Disposer.newDisposable(), pool::close);
    return pool;
  }

  @Override
  public byte[] get(byte[] key) throws Exception {
    StatefulRedisClusterConnection<byte[], byte[]> c = null;
    try {
      c = pool.borrowObject();
      return c.sync().get(key);
    } finally {
      if (c != null) { pool.returnObject(c); }
    }
  }

  @Override
  public boolean test() throws Exception {
    StatefulRedisClusterConnection<byte[], byte[]> c = null;
    try {
      c = pool.borrowObject();
      return "PONG".equals(c.sync().ping());
    } finally {
      if (c != null) { pool.returnObject(c); }
    }
  }
}
