package com.github.xfslove.intellij.plugin.redis.experimental.script;

import com.github.xfslove.intellij.plugin.redis.lang.RedisTypes;
import com.intellij.openapi.util.TextRange;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CellsAccessor {

  private final Object myLock = new Object();
  private final List<Cell> myCells = ContainerUtil.createConcurrentList();
  private boolean myComputed = false;

  @Nullable
  public Cell getCell(int offset) {
    if (this.myCells.isEmpty()) {
      return null;
    }
    int result = ObjectUtils.binarySearch(0, this.myCells.size(), idx -> {
      TextRange range = this.myCells.get(idx).range;
      return range.contains(offset) ? 0 : (range.getStartOffset() > offset ? 1 : -1);
    });

    return result < 0 ? null : this.myCells.get(result);
  }

  /*
   * WARNING - Removed try catching itself - possible behaviour change.
   */

  void compute(ScriptModel<?> model) {
    synchronized (this.myLock) {
      if (this.myComputed) {
        return;
      }

      this.myCells.addAll(getCells(model));
      this.myComputed = true;
    }
  }

  @NotNull
  public Collection<Cell> getCells(ScriptModel<?> model) {

    List<TextRange> curRanges = new ArrayList<>();
    List<Cell> result = new ArrayList<>();

    TextRange first = null;
    TextRange current = null;
    for (CommandIterator<?> statement : model.statements()) {
      current = statement.range();
      first = curRanges.isEmpty() ? current : first;
      curRanges.add(current);

      if (RedisTypes.COMMAND.equals(statement.type())) {

        flushCell(result, curRanges, first, current);
      }

    }
    flushCell(result, curRanges, first, current);

    return result;
  }


  private void flushCell(@NotNull List<Cell> result, @NotNull List<TextRange> curRanges, @Nullable TextRange first, @Nullable TextRange last) {
    if (first == null || last == null) {
      return;
    }
    ArrayList<TextRange> ranges = new ArrayList<>(curRanges);
    curRanges.clear();
    result.add(new Cell(TextRange.create(first.getStartOffset(), last.getEndOffset()), ranges));
  }

}