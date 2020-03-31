package com.github.xfslove.intellij.plugin.redis;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * @author wongiven
 * @date created at 2020/3/26
 */
public class RedisIcon {

  public static Icon get() {
    return IconLoader.getIcon("/icon/redis.svg", RedisIcon.class);
  }
}