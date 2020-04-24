package com.github.xfslove.intellij.plugin.redis.client;

import com.github.xfslove.intellij.plugin.redis.storage.Connection;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.async.RedisStringAsyncCommands;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.async.RedisClusterAsyncCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wongiven
 * @date created at 2020/3/26
 */
public class RedisClient {

  public void connect(Connection connection) {
    RedisClusterAsyncCommands<byte[], byte[]> commands = getCommands(connection);
  }

  public RedisClusterAsyncCommands<byte[], byte[]> getCommands(Connection connection) {
//    if (connection.isCluster()) {

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

      return RedisClusterClient.create(uriList).connect(new ByteArrayCodec()).async();
//    } else {
//      String[] hp = connection.getUrl().split(":");
//      String password = connection.retrievePassword();
//
//      RedisURI uri = RedisURI.Builder.redis(hp[0], Integer.parseInt(hp[1])).build();
//      if (StringUtils.isNotBlank(password)) {
//        uri.setPassword(password);
//      }
//
//      return io.lettuce.core.RedisClient.create(uri).connect(new ByteArrayCodec()).async();
//    }

  }
}
