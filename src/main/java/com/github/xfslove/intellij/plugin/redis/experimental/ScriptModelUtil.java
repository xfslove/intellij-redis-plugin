/*
 * Decompiled with CFR 0.149.
 *
 * Could not load the following classes:
 *  com.intellij.database.script.ScriptModel
 *  com.intellij.database.script.ScriptModel$ChosenRange
 *  com.intellij.database.script.ScriptModel$ModelIt
 *  com.intellij.database.script.ScriptModel$PStorage
 *  com.intellij.database.script.ScriptModel$ParamIt
 *  com.intellij.database.script.ScriptModel$PositionRange
 *  com.intellij.database.script.ScriptModel$SmartRange
 *  com.intellij.database.script.ScriptModel$StatementIt
 *  com.intellij.database.script.ScriptModel$StrictRange
 *  com.intellij.injected.editor.VirtualFileWindow
 *  com.intellij.lang.Language
 *  com.intellij.lang.LanguageUtil
 *  com.intellij.openapi.editor.Caret
 *  com.intellij.openapi.editor.Document
 *  com.intellij.openapi.editor.Editor
 *  com.intellij.openapi.editor.RangeMarker
 *  com.intellij.openapi.editor.actions.EditorActionUtil
 *  com.intellij.openapi.editor.event.DocumentEvent
 *  com.intellij.openapi.editor.event.DocumentListener
 *  com.intellij.openapi.editor.ex.DocumentEx
 *  com.intellij.openapi.editor.ex.util.EditorUtil
 *  com.intellij.openapi.fileEditor.FileDocumentManager
 *  com.intellij.openapi.util.Comparing
 *  com.intellij.openapi.util.Condition
 *  com.intellij.openapi.util.Conditions
 *  com.intellij.openapi.util.Key
 *  com.intellij.openapi.util.Ref
 *  com.intellij.openapi.util.Segment
 *  com.intellij.openapi.util.TextRange
 *  com.intellij.openapi.util.UserDataHolder
 *  com.intellij.openapi.util.text.StringUtil
 *  com.intellij.openapi.vfs.VirtualFile
 *  com.intellij.psi.PsiFile
 *  com.intellij.psi.SingleRootFileViewProvider
 *  com.intellij.psi.SyntaxTraverser
 *  com.intellij.psi.SyntaxTraverser$Api
 *  com.intellij.psi.tree.IElementType
 *  com.intellij.psi.tree.IFileElementType
 *  com.intellij.sql.psi.SqlStatement
 *  com.intellij.testFramework.LightVirtualFile
 *  com.intellij.testFramework.ReadOnlyLightVirtualFile
 *  com.intellij.util.ConcurrencyUtil
 *  com.intellij.util.Function
 *  com.intellij.util.Function$Mono
 *  com.intellij.util.containers.ContainerUtil
 *  com.intellij.util.containers.JBIterable
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.xfslove.intellij.plugin.redis.experimental;

import com.github.xfslove.intellij.plugin.redis.lang.RedisLanguage;
import com.intellij.lang.Language;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.util.*;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.SingleRootFileViewProvider;
import com.intellij.psi.SyntaxTraverser;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.testFramework.ReadOnlyLightVirtualFile;
import com.intellij.util.Function;
import com.intellij.util.containers.JBIterable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScriptModelUtil {
  public static final Key<Long> PART_OFFSET = Key.create( "PART_OFFSET");
  public static final Key<Integer> PART_INDEX = Key.create( "PART_INDEX");
  public static final Key<Integer> PART_ERRORS = Key.create( "PART_ERRORS");
  public static final Function<ScriptModel.ModelIt<?>, TextRange> TO_RANGE = it2 -> it2.range().shiftRight((int) it2.rangeOffset());
  public static final Function<ScriptModel.ModelIt<?>, String> TO_TEXT = it2 -> it2.text();
  public static final Function<ScriptModel.ModelIt<?>, IElementType> TO_TYPE = it2 -> it2.type();
  public static final Function<ScriptModel.ModelIt<?>, Object> TO_OBJECT = it2 -> it2.object();

  private ScriptModelUtil() {
  }

  @NotNull
  public static Condition<TextRange> INTERSECTS_RANGE(TextRange range) {
    return r -> {
      int e;
      int s = Math.max(r.getStartOffset(), range.getStartOffset());
      if (s == (e = Math.min(r.getEndOffset(), range.getEndOffset()))) {
        return s == r.getEndOffset() || range.getLength() == 0;
      }
      return s < e;
    };
  }

  @NotNull
  public static Condition<TextRange> CONTAINS_RANGE(TextRange range) {
    return r -> r.equals(range) || r.contains(range);
  }

  @NotNull
  public static Condition<TextRange> IN_RANGE(TextRange range) {
    return r -> range.contains(r);
  }

  @NotNull
  public static Condition<ScriptModel.ModelIt<?>> AT_OFFSET2(int offset) {
    return o -> o.range().contains(offset);
  }

  @NotNull
  public static <V> Function<SyntaxTraverser<V>, SyntaxTraverser<V>> LIMIT_TO_RANGE(final TextRange range) {
    return (Function.Mono<SyntaxTraverser<V>>) vs -> ScriptModelUtil.inRange(vs, range);
  }

//  @NotNull
//  public static <T> ScriptModel<T> adjustModelForSelection(@NotNull ScriptModel<T> model, @NotNull Document document, @NotNull TextRange selectionRange, @NotNull DatabaseSettings.ExecOption execOption) {
//    TextRange adjustedRange;
//    int fixedOffset = (adjustedRange = ScriptModelUtil.adjustSelectionRange(model, document, selectionRange, execOption)).contains(selectionRange.getStartOffset()) ? selectionRange.getStartOffset() : adjustedRange.getStartOffset();
//    return selectionRange.getLength() == 0 ?
//        model.subModel(new ScriptModel.PositionRange(fixedOffset)) : model.subModel(adjustedRange);
//  }
//
//  @NotNull
//  public static TextRange getSelectedStatementsRange(@NotNull Editor editor) {
//    TextRange range = ScriptModelUtil.getSelectionForConsole(editor);
//    return ScriptModelUtil.adjustSelectionRange(client.getScriptModel(), editor.getDocument(), range);
//  }

//  @NotNull
//  public static TextRange getSelectionForConsole(@Nullable Editor editor) {
//    if (editor == null) {
//      return TextRange.EMPTY_RANGE;
//    }
//    Caret caret = editor.getCaretModel().getPrimaryCaret();
//    return TextRange.create(caret.getSelectionStart(), caret.getSelectionEnd());
//  }
//
//  @NotNull
//  public static TextRange adjustSelectionRange(@NotNull ScriptModel<?> model, @NotNull Document document, @NotNull TextRange selectionRange) {
//    if (selectionRange.isEmpty()) {
//      TextRange subRange;
//      TextRange r;
//      int lineEnd;
//      int lineNumber;
//      int lineStart;
//      int lineStartFixed;
//      CharSequence sequence = document.getCharsSequence();
//      int sequenceLength = sequence.length();
//      if (sequenceLength == 0) {
//        return selectionRange;
//      }
//      int offset = Math.min(sequenceLength - 1, selectionRange.getStartOffset());
//      if (!Character.isWhitespace(sequence.charAt(offset))) {
//        ++offset;
//      }
//      lineStart = document.getLineStartOffset(lineNumber = document.getLineNumber(offset));
//      lineEnd = document.getLineEndOffset(lineNumber);
//      lineStartFixed = EditorActionUtil.findFirstNonSpaceOffsetInRange(sequence, lineStart, (lineEnd));
//      subRange = TextRange.create(lineStartFixed, (Math.max(lineStartFixed, offset)));
//      ScriptModel.PositionRange positionRange = new ScriptModel.PositionRange(subRange);
//      r = model.subModel(positionRange).statements().filter(Conditions.compose(TO_OBJECT, Conditions.instanceOf(RedisCommand.class))).transform(TO_RANGE).last();
//      if (lineStartFixed >= 0 && r != null) {
//        return r;
//      }
//      return selectionRange;
//    }
////    if (execOption.execSelection == 1) {
////      return new ScriptModel.StrictRange(selectionRange);
////    }
////    if (execOption.execSelection == 2) {
////      return selectionRange;
////    }
////    if (execOption.execSelection == 3) {
////      ScriptModel.SmartRange smartRange = new ScriptModel.SmartRange(selectionRange);
////      ScriptModel<?> m = model.subModel(smartRange);
////      if (m.statements().take(2).size() > 1) {
////        TextRange r = selectionRange;
////        for (TextRange range : m.statements().transform(TO_RANGE)) {
////          r = r.union(range);
////        }
////        return new ScriptModel.SmartRange(r);
////      }
////      return smartRange;
////    }
//    throw new AssertionError();
//  }

  @NotNull
  public static <V> SyntaxTraverser<V> inRange(@NotNull SyntaxTraverser<V> s, @NotNull TextRange range) {
    int partOffset = ScriptModelUtil.getPartOffsetAsInt(s);
    if (range.getStartOffset() < partOffset) {
      return s.expandAndFilter(Conditions.alwaysFalse());
    }
    TextRange adjusted = range.shiftRight(-partOffset);
    SyntaxTraverser<V> ranged = s.onRange(Conditions.compose(s.api.TO_RANGE, ScriptModelUtil.INTERSECTS_RANGE(adjusted)));
    return range instanceof ScriptModel.ChosenRange ?
        ranged.filter(Conditions.compose(s.api.TO_RANGE, ScriptModelUtil.IN_RANGE(adjusted))) : ranged;
  }

  public static int getPartOffsetAsInt(SyntaxTraverser<?> s) {
    return (int) ScriptModelUtil.getPartOffset(s);
  }

  public static long getPartOffset(SyntaxTraverser<?> s) {
    Long o = s.getUserData(PART_OFFSET);
    return o == null ? 0L : o;
  }

  public static <V> String statementText(ScriptModel.StatementIt<V> st) {
    return st.text();
//    return ScriptModelUtil.statementText(st.text(), st.range().getStartOffset());
  }

  @NotNull
  public static String statementText(@NotNull String text2, int offset) {
    int curIndex = 0;
    StringBuilder sb = null;
    return text2;
  }

  @NotNull
  public static <V> Iterable<String> getRelativeSnippet(V current, SyntaxTraverser.Api<V> api) {
    V parent2 = api.parent(current);
    if (parent2 == null) {
      return JBIterable.empty();
    }
    TextRange pr = api.rangeOf(parent2);
    TextRange cr = api.rangeOf(current);
    int delta = 15;
    int a2 = cr.getStartOffset() - pr.getStartOffset();
    int b2 = cr.getEndOffset() - pr.getStartOffset();
    int s = Math.max(a2 - delta, 0);
    int e = Math.min(b2 + delta, pr.getLength());
    int a1 = s < delta ? 0 : s;
    int b1 = pr.getLength() - e < delta ? pr.getLength() : e;
    CharSequence t = api.textOf(parent2);
    return JBIterable.of((a1 > 0 ? "..." : "") + t.subSequence(a1, a2), t.subSequence(a2, b2).toString(), t.subSequence(b2, b1) + (b1 < pr.getLength() ? "..." : ""));
  }

  public static int getSelectionOption(TextRange range) {
    return range instanceof ScriptModel.StrictRange ? 1 : (range instanceof ScriptModel.SmartRange ? 3 : 2);
  }

  @Nullable
  public static Language getJSLanguage() {
    Language lang = Language.findLanguageByID("REDIS");
    if (lang != null) {
      return lang;
    }
    lang = Language.findLanguageByID("REDIS");
    if (lang != null) {
      return lang;
    }
    return RedisLanguage.INSTANCE;
  }

  @NotNull
  public static <V> Condition<V> wholeFileCondition(SyntaxTraverser<V> s, TextRange scriptRange, Condition<? super IElementType> wsOrComment, Condition<V> isStatement) {
    return v -> {
      if (!(s.api.typeOf(v) instanceof IFileElementType)) {
        return false;
      }
      JBIterable<V> it2 = ((s.withRoot(v)).reset()).preOrderDfsTraversal().skip(1);
      V st = it2.filter(isStatement).first();
      if (st == null) {
        return s.api.rangeOf(v).getLength() > 256 || !StringUtil.isEmptyOrSpaces(s.api.textOf(v));
      }
      if (!(scriptRange instanceof ScriptModel.PositionRange) && scriptRange.getLength() > 0) {
        if (scriptRange instanceof ScriptModel.StrictRange) {
          return !Comparing.equal(s.api.rangeOf(v), s.api.rangeOf(st));
        }
        return s.api.rangeOf(st).getStartOffset() > 0 && it2.takeWhile(Conditions.notEqualTo(st)).filter(o -> !wsOrComment.value(s.api.typeOf(o))).isNotEmpty();
      }
      return (s.withRoot(v)).preOrderDfsTraversal().take(3).size() > 2;
    };
  }

  @Nullable
  public static Document getScriptDocument(VirtualFile virtualFile) {
    return virtualFile instanceof ReadOnlyLightVirtualFile || SingleRootFileViewProvider.isTooLargeForIntelligence(virtualFile) ? null : FileDocumentManager.getInstance().getDocument(virtualFile);
  }

}

