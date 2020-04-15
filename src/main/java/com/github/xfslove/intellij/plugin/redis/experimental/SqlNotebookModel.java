package com.github.xfslove.intellij.plugin.redis.experimental;

import com.github.xfslove.intellij.plugin.redis.experimental.script.ScriptModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import com.intellij.psi.SingleRootFileViewProvider;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.util.ArrayUtil;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SqlNotebookModel {

  private SqlNotebookModel() {
  }

  @Nullable
  public static CellsAccessor getCellAccessor(@NotNull PsiFile file, ScriptModel<?> model, @NotNull Condition<? super TextRange> filter2) {
    if (SingleRootFileViewProvider.isTooLargeForIntelligence(file.getVirtualFile())) {
      return null;
    }
    return getPreparedAccessor(file, model, filter2);
  }

  @Nullable
  public static SqlNotebookManager.Cell getCell(@NotNull PsiFile file, ScriptModel<?> model, @NotNull EditorLayoutProvider layoutProvider, @NotNull Condition<? super TextRange> filter2, int offset) {
    if (SingleRootFileViewProvider.isTooLargeForIntelligence(file.getVirtualFile())) {
      return null;
    }
    return SqlNotebookModel.getPreparedAccessor(file, model, filter2).getCell(layoutProvider, offset);
  }

  @NotNull
  public static Collection<SqlNotebookManager.Cell> getCells(@NotNull PsiFile file, ScriptModel<?> model, @NotNull EditorLayoutProvider layoutProvider, @NotNull Condition<? super TextRange> filter2) {
    if (SingleRootFileViewProvider.isTooLargeForIntelligence(file.getVirtualFile())) {
      return Collections.emptyList();
    }
    return SqlNotebookModel.getPreparedAccessor(file, model, filter2).getCells(layoutProvider);
  }

  @NotNull
  public static Collection<SqlNotebookManager.Cell> getCachedCells(@NotNull PsiFile file) {
    return SqlNotebookModel.getCachedAccessor(file).getCells(EditorLayoutProvider.DUMMY);
  }

  @NotNull
  private static CellsAccessor getPreparedAccessor(@NotNull PsiFile file, ScriptModel<?> model, @NotNull Condition<? super TextRange> filter2) {
    CellsAccessor accessor = SqlNotebookModel.getCachedAccessor(file);
    accessor.compute(file.getProject(), file,model, filter2);
    return accessor;
  }

  @NotNull
  private static CellsAccessor getCachedAccessor(@NotNull PsiFile file) {
    return (CellsAccessor) CachedValuesManager.getCachedValue(file, () -> CachedValueProvider.Result.create((Object) new CellsAccessor(), file));

  }

  public interface EditorLayoutProvider {
    EditorLayoutProvider DUMMY = new EditorLayoutProvider() {

      @Override
      public int[] getInlayOffsets(@NotNull TextRange range) {
        return ArrayUtil.EMPTY_INT_ARRAY;
      }

      @Override
      public int getInlayLength() {
        return 0;
      }
    };

    int[] getInlayOffsets(@NotNull TextRange range);

    int getInlayLength();
  }

  public static class CellsAccessor {

    private final Object myLock = new Object();
    private final List<SqlNotebookManager.Cell> myCells = ContainerUtil.createConcurrentList();
    private boolean myComputed = false;

    @Nullable
    public SqlNotebookManager.Cell getCell(@NotNull EditorLayoutProvider provider, int offset) {
      if (this.myCells.isEmpty()) {
        return null;
      }
      int result = ObjectUtils.binarySearch(0, this.myCells.size(), idx -> {
        TextRange range = this.myCells.get(idx).range;
        return range.contains(offset) ? 0 : (range.getStartOffset() > offset ? 1 : -1);
      });
      SqlNotebookManager.Cell resultCell = result < 0 ? null : this.myCells.get(result);
      Collection<SqlNotebookManager.Cell> cells = CellsAccessor.splitCell(resultCell, provider);
      for (SqlNotebookManager.Cell cell : cells) {
        if (!cell.range.contains(offset)) {
          continue;
        }
        return cell;
      }
      return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void compute(@NotNull Project project, @NotNull PsiFile file, ScriptModel<?> model, @NotNull Condition<? super TextRange> filter2) {
      synchronized (this.myLock) {
        if (this.myComputed) {
          return;
        }

        this.myCells.addAll(SqlNotebookManager.INSTANCE.getCells(file, filter2,model, 0, file.getTextLength()));
        this.myComputed = true;
      }
    }

    @NotNull
    Collection<SqlNotebookManager.Cell> getCells(@NotNull EditorLayoutProvider provider) {
      ArrayList<SqlNotebookManager.Cell> result = new ArrayList<>();
      for (SqlNotebookManager.Cell cell : this.myCells) {
        result.addAll(CellsAccessor.splitCell(cell, provider));
      }
      return result;
    }

    @NotNull
    private static Collection<SqlNotebookManager.Cell> splitCell(@Nullable SqlNotebookManager.Cell cell, @NotNull EditorLayoutProvider provider) {
      if (cell == null) {
        return Collections.emptyList();
      }
      int[] inlayOffsets = provider.getInlayOffsets(cell.range);
      if (inlayOffsets.length == 0 || inlayOffsets.length == 1 && cell.lastStatementEnd <= inlayOffsets[0]) {
        return Collections.singleton(cell);
      }
      ArrayList<TextRange> ranges = new ArrayList<>(cell.ranges);
      Collection<CellItem> items = CellsAccessor.merge(ranges, inlayOffsets, provider.getInlayLength());
      ArrayList<SqlNotebookManager.Cell> result = new ArrayList<>();
      ArrayList<TextRange> curRanges = new ArrayList<>();
      int start2 = -1;
      int cellStart = -1;
      for (CellItem item : items) {
        if (start2 == -1) {
          start2 = item.range.getStartOffset();
        }
        if (cellStart != -1) {
          CellsAccessor.flushCell(curRanges, result, cellStart, item.range.getStartOffset());
          cellStart = -1;
          start2 = item.range.getStartOffset();
        }
        if (!item.statement) {
          cellStart = start2;
          continue;
        }
        curRanges.add(item.range);
      }
      CellsAccessor.flushCell(curRanges, result, start2, cell.range.getEndOffset());
      return result;
    }

    private static void flushCell(@NotNull List<TextRange> ranges, @NotNull List<SqlNotebookManager.Cell> result, int start2, int end2) {
      TextRange lastRange = ContainerUtil.getLastItem(ranges);
      int lastStatementEnd = lastRange == null ? start2 : lastRange.getEndOffset();
      result.add(new SqlNotebookManager.Cell(TextRange.create(start2, end2), new ArrayList<>(ranges), lastStatementEnd));
      ranges.clear();
    }

    @NotNull
    private static Collection<CellItem> merge(@NotNull List<TextRange> ranges, int[] offsets, int inlayLength) {
      ArrayList<CellItem> items = new ArrayList<>();
      int inlayOffset = 0;
      int rangeOffset = 0;
      CellItem firstInlay = null;
      while (inlayOffset < offsets.length || rangeOffset < ranges.size()) {
        TextRange range = rangeOffset < ranges.size() ? ranges.get(rangeOffset) : null;
        int inlay = inlayOffset < offsets.length ? offsets[inlayOffset] : -1;
        if (range == null || inlay != -1 && range.getEndOffset() > inlay) {
          CellItem item = new CellItem(TextRange.create(inlay, inlay + inlayLength), false);
          items.add(item);
          if (firstInlay == null) {
            firstInlay = item;
          }
          ++inlayOffset;
          continue;
        }
        items.add(new CellItem(firstInlay == null || !range.intersectsStrict(firstInlay.range) ? range : TextRange.create(range.getStartOffset(), firstInlay.range.getStartOffset()), true));
        firstInlay = null;
        ++rangeOffset;
      }
      return ContainerUtil.sorted(items, Comparator.comparingInt(i2 -> i2.range.getStartOffset()));
    }

    private static class CellItem {
      final TextRange range;
      final boolean statement;

      CellItem(@NotNull TextRange range, boolean statement) {
        this.range = range;
        this.statement = statement;
      }

    }
  }
}

