//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.xfslove.intellij.plugin.redis.experimental;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.SyntaxTraverser;
import com.intellij.psi.SyntaxTraverser.Api;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.JBIterable;
import com.intellij.util.containers.JBIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ScriptModel<E> implements Disposable {

  public abstract ScriptModel<E> subModel(@Nullable TextRange textRange);

  public abstract JBIterable<? extends ScriptModel.StatementIt<E>> statements();

  public abstract VirtualFile getVirtualFile();

  public abstract TextRange getTextRange();

  @Override
  public void dispose() {
  }

  @Override
  public String toString() {
    return "ScriptModel{range=" + this.getTextRange() + ", file=" + this.getVirtualFile() + "}";
  }

  public static final class ChosenRange extends TextRange {
    public ChosenRange(TextRange range) {
      super(range.getStartOffset(), range.getEndOffset(), true);
    }
  }

  public static final class PositionRange extends TextRange {
    public PositionRange(int position) {
      super(position, position, true);
    }

    public PositionRange(TextRange range) {
      super(range.getStartOffset(), range.getEndOffset(), true);
    }
  }

  public static final class StrictRange extends TextRange {
    public StrictRange(@NotNull TextRange range) {
      super(range.getStartOffset(), range.getEndOffset(), true);
    }
  }

  public static final class SmartRange extends TextRange {
    public SmartRange(@NotNull TextRange range) {
      super(range.getStartOffset(), range.getEndOffset(), true);
    }
  }

  public abstract static class ModelItBase<E> extends JBIterator<E> implements ScriptModel.ModelIt<E> {

    protected SyntaxTraverser<E> traverser;

    @Override
    public long rangeOffset() {
      return 0L;
    }

    @Override
    public final E object() {
      return this.current();
    }

    @Override
    public final String text() {
      return this.traverser.api.textOf(object()).toString();
    }

    @Override
    public final TextRange range() {
      return this.traverser.api.rangeOf(object());
    }

    @Override
    public final IElementType type() {
      return this.traverser.api.typeOf(object());
    }

    @Override
    public Api<E> api() {
      return this.traverser.api;
    }

    public final JBIterable<? extends ModelItBase<E>> cursor() {
      return JBIterator.cursor(this);
    }
  }

  public interface StatementIt<E> extends ScriptModel.ModelIt<E> {

    String stText();
  }

  public interface ModelIt<E> {

    String text();

    TextRange range();

    IElementType type();

    Api<E> api();

    long rangeOffset();

    E object();
  }
}
