// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.github.xfslove.intellij.plugin.redis.lang.psi;

import com.github.xfslove.intellij.plugin.redis.RedisIcon;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class RedisFileType extends LanguageFileType {

  public static final RedisFileType INSTANCE = new RedisFileType();

  private RedisFileType() {
    super(RedisLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public String getName() {
    return "Redis Plugin";
  }

  @NotNull
  @Override
  public String getDescription() {
    return "Redis Plugin";
  }

  @NotNull
  @Override
  public String getDefaultExtension() {
    return "redis";
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return RedisIcon.get();
  }
}