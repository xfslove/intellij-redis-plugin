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
package com.github.xfslove.intellij.plugin.redis.experimental.script;

import com.github.xfslove.intellij.plugin.redis.experimental.PositionRange;
import com.github.xfslove.intellij.plugin.redis.lang.RedisCommand;
import com.intellij.lang.Language;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actions.EditorActionUtil;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Conditions;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.SingleRootFileViewProvider;
import com.intellij.psi.SyntaxTraverser;
import com.intellij.testFramework.ReadOnlyLightVirtualFile;
import com.intellij.util.containers.JBIterable;
import com.intellij.util.containers.TreeTraversal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScriptModelUtil {

  public static final Key<Long> PART_OFFSET = Key.create("PART_OFFSET");

  @NotNull
  public static <E> SyntaxTraverser<E> parse(@NotNull Project project, @NotNull CharSequence documentText, @NotNull Language language) {
    ReadOnlyLightVirtualFile file = new ReadOnlyLightVirtualFile("dummy.redis", language, documentText);
    SingleRootFileViewProvider.doNotCheckFileSizeLimit(file);
    PsiManager psiManager = PsiManager.getInstance(project);
    SingleRootFileViewProvider viewProvider = new SingleRootFileViewProvider(psiManager, file, false);
    PsiFile psiFile = viewProvider.getPsi(language);
    return (SyntaxTraverser<E>) SyntaxTraverser.psiTraverser(psiFile).expand(Conditions.alwaysFalse()).withTraversal(TreeTraversal.LEAVES_DFS);
  }

  @NotNull
  public static TextRange getSelectedCommandRange(@NotNull ScriptModel<?> model, @NotNull Editor editor) {
    TextRange range = ScriptModelUtil.getSelectionForConsole(editor);
    return ScriptModelUtil.adjustSelectionRange(model, editor.getDocument(), range);
  }

  @NotNull
  public static TextRange getSelectionForConsole(@Nullable Editor editor) {
    if (editor == null) {
      return TextRange.EMPTY_RANGE;
    }
    Caret caret = editor.getCaretModel().getPrimaryCaret();
    return TextRange.create(caret.getSelectionStart(), caret.getSelectionEnd());
  }

  @NotNull
  public static TextRange adjustSelectionRange(@NotNull ScriptModel<?> model, @NotNull Document document, @NotNull TextRange selectionRange) {
    if (selectionRange.isEmpty()) {

      CharSequence sequence = document.getCharsSequence();
      int sequenceLength = sequence.length();
      if (sequenceLength == 0) {
        return selectionRange;
      }
      int offset = Math.min(sequenceLength - 1, selectionRange.getStartOffset());
      if (!Character.isWhitespace(sequence.charAt(offset))) {
        ++offset;
      }
      int lineNumber = document.getLineNumber(offset);
      int lineStart = document.getLineStartOffset(lineNumber);
      int lineEnd = document.getLineEndOffset(lineNumber);
      int lineStartFixed = EditorActionUtil.findFirstNonSpaceOffsetInRange(sequence, lineStart, lineEnd);
      TextRange subRange = TextRange.create(lineStartFixed, Math.max(lineStartFixed, offset));
      PositionRange positionRange = new PositionRange(subRange);

      // filter last command range
      TextRange range = model
          .subModel(positionRange)
          .statements()
          .filter(Conditions.compose(ModelIterator::object, Conditions.instanceOf(RedisCommand.class)))
          .transform(it2 -> it2.range().shiftRight((int) it2.rangeOffset()))
          .last();
      if (lineStartFixed >= 0 && range != null) {
        return range;
      }

      return selectionRange;
    }

    return selectionRange;

  }

  public static long getPartOffset(SyntaxTraverser<?> s) {
    Long o = s.getUserData(PART_OFFSET);
    return o == null ? 0L : o;
  }

  @Nullable
  public static Document getScriptDocument(VirtualFile virtualFile) {
    return virtualFile instanceof ReadOnlyLightVirtualFile || SingleRootFileViewProvider.isTooLargeForIntelligence(virtualFile) ?
        null : FileDocumentManager.getInstance().getDocument(virtualFile);
  }

}

