package com.github.xfslove.intellij.plugin.redis.experimental.script;

import com.intellij.openapi.util.TextRange;

import java.util.List;
import java.util.Objects;

public final class Cell {

  public final TextRange range;
  public final List<TextRange> ranges;
  public final int lastStatementEnd;

  public Cell(TextRange range, List<TextRange> ranges) {
    this.range = range;
    this.ranges = ranges;
    this.lastStatementEnd = range.getEndOffset();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o != null && this.getClass() == o.getClass()) {
      Cell cell = (Cell) o;
      return
          this.lastStatementEnd == cell.lastStatementEnd &&
              this.range.equals(cell.range) &&
              this.ranges.equals(cell.ranges);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.range, this.ranges, this.lastStatementEnd);
  }
}