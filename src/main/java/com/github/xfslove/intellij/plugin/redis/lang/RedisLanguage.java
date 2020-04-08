package com.github.xfslove.intellij.plugin.redis.lang;

import com.intellij.lang.Language;
import org.jetbrains.annotations.NonNls;

/**
 * @author wongiven
 * @date created at 2020/3/31
 */
public class RedisLanguage extends Language {

  public static final RedisLanguage INSTANCE = new RedisLanguage();

  private RedisLanguage() {
    this("REDIS");
  }

  protected RedisLanguage(@NonNls String name) {
    super(name);
  }
}
