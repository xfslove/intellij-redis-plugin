// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.github.xfslove.intellij.plugin.redis.lang;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class RedisFile extends PsiFileBase {

  public RedisFile(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, RedisLanguage.INSTANCE);
  }
  
  @NotNull
  @Override
  public FileType getFileType() {
    return RedisFileType.INSTANCE;
  }
  
  @Override
  public String toString() {
    return "Redis Plugin";
  }
}