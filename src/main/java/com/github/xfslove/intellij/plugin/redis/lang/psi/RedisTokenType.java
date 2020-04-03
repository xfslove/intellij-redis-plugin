// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.github.xfslove.intellij.plugin.redis.lang.psi;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class RedisTokenType extends IElementType {

  public RedisTokenType(@NotNull @NonNls String debugName) {
    super(debugName, RedisLanguage.INSTANCE);
  }
  
  @Override
  public String toString() {
    return "RedisTokenType." + super.toString();
  }
}
