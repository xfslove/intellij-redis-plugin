// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.github.xfslove.intellij.plugin.redis.lang;

import com.github.xfslove.intellij.plugin.redis.lang._RedisLexer;
import com.intellij.lexer.FlexAdapter;

public class RedisLexerAdapter extends FlexAdapter {

  public RedisLexerAdapter() {
    super(new _RedisLexer(null));
  }
}
