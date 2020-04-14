/*
 * Decompiled with CFR 0.149.
 *
 * Could not load the following classes:
 *  com.intellij.database.script.ScriptModel
 *  com.intellij.database.script.ScriptModel$StatementIt
 *  com.intellij.lang.LanguageParserDefinitions
 *  com.intellij.lang.ParserDefinition
 *  com.intellij.openapi.util.Condition
 *  com.intellij.openapi.util.TextRange
 *  com.intellij.psi.PsiElement
 *  com.intellij.psi.PsiFile
 *  com.intellij.psi.SyntaxTraverser$Api
 *  com.intellij.psi.SyntaxTraverser$ApiEx
 *  com.intellij.psi.tree.IElementType
 *  com.intellij.psi.tree.TokenSet
 *  com.intellij.sql.database.SqlNotebookManager
 *  com.intellij.sql.database.SqlNotebookManager$Cell
 *  com.intellij.sql.dialects.EvaluationHelper
 *  com.intellij.sql.psi.SqlSetStatement
 *  com.intellij.sql.psi.SqlUseDatabaseStatement
 *  com.intellij.util.ObjectUtils
 *  com.intellij.util.containers.ContainerUtil
 *  com.intellij.util.containers.JBIterable
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.xfslove.intellij.plugin.redis.experimental;

import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.ParserDefinition;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import com.intellij.psi.SyntaxTraverser;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.JBIterable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class SqlNotebookManager {

  public static final SqlNotebookManager INSTANCE = new SqlNotebookManager();

  public static final class Cell {

    public static final int UNDEFINED = -1;
    public final TextRange range;
    public final List<TextRange> ranges;
    public final int lastStatementEnd;

    public Cell(TextRange range, List<TextRange> ranges, int end) {
      this.range = range;
      this.ranges = ranges;
      this.lastStatementEnd = end == UNDEFINED ? range.getEndOffset() : end;
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

  @NotNull
  public <E> Collection<Cell> getCells(
      @NotNull PsiFile file,
      @NotNull Condition<? super TextRange> filter2,
      ScriptModel<E> model,
      int start2, int end2) {

    TextRange first2 = null;
    TextRange last = null;
    ArrayList<TextRange> curRanges = new ArrayList<>();
    ArrayList<Cell> result = new ArrayList<>();

    for (ScriptModel.StatementIt<E> statement : model.statements()) {
      TextRange current = statement.range();
      if (current.getEndOffset() <= start2 || statement.object() instanceof PsiFile || !filter2.value(current)) {
        continue;
      }
      TextRange withComments = this.captureComments(statement);
      boolean nextCell = last == null || !current.equals( withComments) || isCrossCellGap(file, statement, last, current);
      if (first2 == null || nextCell) {
        flushCell(result, curRanges, file, first2, last, withComments);
        if (current.getStartOffset() > end2) {
          first2 = null;
          last = null;
          break;
        }
        first2 = current = isResultsFree(statement) ? null : withComments;
      }
      ContainerUtil.addAllNotNull(curRanges, current);
      last = current;
    }
    flushCell(result, curRanges, file, first2, last, null);
    return result;
  }

  @Nullable
  public <E> E getPrecedingComment(@Nullable E element2, @NotNull SyntaxTraverser.Api<E> api) {
    E prevElement;
    return (prevElement = this.skipWhitespacesBackward(element2, api)) != null && isComment(api.typeOf(prevElement)) ? (E) prevElement : null;
  }

  private static boolean isComment(@NotNull IElementType type) {
    ParserDefinition def = LanguageParserDefinitions.INSTANCE.forLanguage(type.getLanguage());
    return def != null && def.getCommentTokens().contains(type);
  }

  @Nullable
  public <E> E skipWhitespacesBackward(@Nullable E element2, @NotNull SyntaxTraverser.Api<E> api) {
    if (element2 == null) {
      return null;
    }
    return getPrevSiblingsReversed(element2, api).find(sibling -> !TokenSet.WHITE_SPACE.contains(api.typeOf(sibling)));
  }

  @NotNull
  private static <E> JBIterable<E> getPrevSiblingsReversed(E element2, SyntaxTraverser.Api<E> api) {
    if (api instanceof SyntaxTraverser.ApiEx) {
      SyntaxTraverser.ApiEx apiEx = (SyntaxTraverser.ApiEx) api;
      JBIterable jBIterable = JBIterable.generate(apiEx.previous(element2), sibling -> apiEx.previous(sibling));
      return jBIterable;
    }
    Object parent2 = api.parent(element2);
    if (parent2 == null) {
      return JBIterable.empty();
    }
    JBIterable children2 = api.children((E) parent2);
    return JBIterable.from(ContainerUtil.reverse(children2.take(children2.indexOf(child -> child == element2)).toList()));
  }

  @NotNull
  private <E> TextRange captureComments(@NotNull ScriptModel.StatementIt<E> statement) {
    SyntaxTraverser.Api api;
    Object element2;
    Object comment;
    return (comment = this.getPrecedingComment(element2 = statement.object(), api = statement.api())) == null ? api.rangeOf(element2) : TextRange.create((int) api.rangeOf(comment).getStartOffset(), (int) api.rangeOf(element2).getEndOffset());
  }

  private static void flushCell(@NotNull List<Cell> result, @NotNull List<TextRange> curRanges, @NotNull PsiFile file, @Nullable TextRange first2, @Nullable TextRange last, @Nullable TextRange nextCellStart) {
    if (first2 == null || last == null) {
      return;
    }
    int cellEnd = nextCellStart == null ? file.getTextLength() : nextCellStart.getStartOffset();
    ArrayList<TextRange> ranges = new ArrayList<>(curRanges);
    curRanges.clear();
    result.add(new Cell(TextRange.create(first2.getStartOffset(), cellEnd), ranges, last.getEndOffset()));
  }

  private static boolean isCrossCellGap(@NotNull PsiFile file, @NotNull ScriptModel.StatementIt<?> statement, @NotNull TextRange prev, @NotNull TextRange range) {
    if (isResultsFree(statement)) {
      return true;
    }
    String text2 = file.getText();
    return text2.substring(prev.getEndOffset(), range.getStartOffset()).contains("\n\n");
  }

  private static boolean isResultsFree(@NotNull ScriptModel.StatementIt<?> statement) {
//    PsiElement element2 = ObjectUtils.tryCast(statement.object(), PsiElement.class);
    return false;
  }

}

