package com.github.xfslove.intellij.plugin.redis.experimental;

import com.intellij.openapi.util.TextRange;

public final class PositionRange extends TextRange {

    public PositionRange(TextRange range) {
      super(range.getStartOffset(), range.getEndOffset(), true);
    }
  }