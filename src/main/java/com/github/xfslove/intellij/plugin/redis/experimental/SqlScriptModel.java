/*
 * Decompiled with CFR 0.149.
 *
 * Could not load the following classes:
 *  com.intellij.database.Dbms
 *  com.intellij.database.script.ScriptModel
 *  com.intellij.database.script.ScriptModel$ChosenRange
 *  com.intellij.database.script.ScriptModel$ModelItBase
 *  com.intellij.database.script.ScriptModel$PStorage
 *  com.intellij.database.script.ScriptModel$ParamIt
 *  com.intellij.database.script.ScriptModel$PositionRange
 *  com.intellij.database.script.ScriptModel$StatementIt
 *  com.intellij.lang.Language
 *  com.intellij.lang.LanguageUtil
 *  com.intellij.lang.LighterASTNode
 *  com.intellij.openapi.Disposable
 *  com.intellij.openapi.editor.Document
 *  com.intellij.openapi.fileTypes.PlainTextLanguage
 *  com.intellij.openapi.project.Project
 *  com.intellij.openapi.util.Condition
 *  com.intellij.openapi.util.Conditions
 *  com.intellij.openapi.util.Disposer
 *  com.intellij.openapi.util.TextRange
 *  com.intellij.openapi.util.UserDataHolder
 *  com.intellij.openapi.util.text.StringUtil
 *  com.intellij.openapi.vfs.VirtualFile
 *  com.intellij.psi.PsiElement
 *  com.intellij.psi.PsiFile
 *  com.intellij.psi.SyntaxTraverser
 *  com.intellij.sql.dialects.EvaluationHelper
 *  com.intellij.sql.dialects.SqlLanguageDialect
 *  com.intellij.sql.psi.SqlBatchBlock
 *  com.intellij.sql.psi.SqlParameter
 *  com.intellij.sql.psi.SqlTableType
 *  com.intellij.sql.script.Script
 *  com.intellij.util.Function
 *  com.intellij.util.containers.JBIterable
 *  com.intellij.util.containers.JBIterator
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.xfslove.intellij.plugin.redis.experimental;

import com.intellij.lang.Language;
import com.intellij.lang.LanguageUtil;
import com.intellij.lang.LighterASTNode;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.*;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.SyntaxTraverser;
import com.intellij.util.Function;
import com.intellij.util.containers.JBIterable;
import com.intellij.util.containers.JBIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SqlScriptModel<E> extends ScriptModel<E> {

  private final Script<E> myScript;

  public SqlScriptModel(@NotNull PsiFile file) {
    this(file.getProject(), file.getViewProvider().getVirtualFile(), null);
  }

  public SqlScriptModel(@NotNull Project project, @NotNull VirtualFile virtualFile, @Nullable Language language) {
    this(SqlScriptModel.newScriptFor(project, virtualFile, language));
  }

  private SqlScriptModel(@NotNull Script<E> script) {
    this.myScript = script;
  }

  @NotNull
  private static <E> Script<E> newScriptFor(@NotNull Project project, @NotNull VirtualFile virtualFile, @Nullable Language language) {
    boolean needParsing = language != null && SqlScriptModel.getLanguage(virtualFile, project) != language;
    Document document = needParsing ? null : ScriptModelUtil.getScriptDocument(virtualFile);
    if (document != null) {
      return new TrackingDocScript<>(project, virtualFile, document);
    }
    return new FileScript<>(project, virtualFile, null, language);
  }

  @NotNull
  private Project getProject() {
    return this.myScript.getProject();
  }

  @Override
  public VirtualFile getVirtualFile() {
    return this.myScript.getVirtualFile();
  }

  @Override
  public SqlScriptModel subModel(@Nullable TextRange range) {
    return new SqlScriptModel(this.myScript.subScript(range));
  }

  @Override
  public JBIterable<? extends ScriptModel.StatementIt<E>> statements() {
    RedisEvaluationHelper helper = new RedisEvaluationHelper();
    SIt sIt = new SIt(this.myScript.getScript(), s -> helper.statements(this.myScript, s));
    return (JBIterable<? extends ScriptModel.StatementIt<E>>) sIt.cursor();
  }

  @Override
  public TextRange getTextRange() {
    return this.myScript.getRange();
  }

  @Override
  public void dispose() {
    super.dispose();
    if (this.myScript instanceof Disposable) {
      Disposer.dispose((Disposable) this.myScript);
    }
  }

  public static Language getLanguage(@NotNull VirtualFile vFile, Project project) {
    return LanguageUtil.getLanguageForPsi(project, vFile);
  }

  private class SIt extends MIt implements ScriptModel.StatementIt<E> {

    SIt(JBIterable<SyntaxTraverser<E>> s, Function<SyntaxTraverser<E>, ? extends Iterable<E>> transform) {
      super(s, transform);
    }

    @Override
    public String stText() {
      return ScriptModelUtil.statementText(this);
    }

  }

  private class MIt extends ScriptModel.ModelItBase<E> {

    long rangeOffset;
    JBIterator<E> vIt;
    SyntaxTraverser<E> nextTr;

    MIt(JBIterable<SyntaxTraverser<E>> s, Function<SyntaxTraverser<E>, ? extends Iterable<E>> transform) {
      this.vIt = from(s.flatten(vs -> {
        this.nextTr = vs;
        return transform.fun(vs);
      }).iterator());
    }

    @Override
    protected E nextImpl() {
      return this.vIt.hasNext() ? this.vIt.next() : this.stop();
    }

    @Override
    protected void currentChanged() {
      if (this.nextTr == null) {
        return;
      }
      this.traverser = this.nextTr;
      this.nextTr = null;
      this.rangeOffset = ScriptModelUtil.getPartOffset(this.traverser);
    }

    @Override
    public long rangeOffset() {
      return this.rangeOffset;
    }
  }

  private static class FileScript<E> extends ScriptBase<E> implements Disposable {

    final Language forcedLanguage;

    FileScript(Project project, VirtualFile virtualFile, TextRange range, Language forcedLanguage) {
      super(project, virtualFile, range);
      this.forcedLanguage = forcedLanguage;
    }

    @Override
    @NotNull
    public Language getLanguage() {
      return this.forcedLanguage == null ? super.getLanguage() : this.forcedLanguage;
    }

    @Override
    public UserDataHolder getParamDataHolder() {
      return this.vFile;
    }

    @Override
    public FileScript<E> subScript(@Nullable TextRange range) {
      return new FileScript<>(this.project, this.vFile, range, this.forcedLanguage);
    }

    @Override
    public JBIterable<SyntaxTraverser<E>> getScript() {
      boolean atPosition = this.range instanceof ScriptModel.PositionRange;
      boolean inRange = !atPosition && this.range != null && this.range.getLength() > 0;
      Language language = this.getLanguage();
      JBIterable result = parseFile(this.project, language, this.vFile, this, new RedisEvaluationHelper());
      if (inRange) {
        result = result.transform(ScriptModelUtil.LIMIT_TO_RANGE(this.range));
      }
      return result;
    }

    @Override
    public void dispose() {
    }

  }

  @NotNull
  public static <E> JBIterable<SyntaxTraverser<E>> parseFile(final @NotNull Project project, final @NotNull Language language, final @NotNull VirtualFile virtualFile, final @NotNull Disposable disposable2, final @NotNull EvaluationHelper<E> evaluationHelper) {
    Document document = ScriptModelUtil.getScriptDocument(virtualFile);
    return JBIterable.of(evaluationHelper.parse(project, language, document.getCharsSequence()));
  }

  private static class SealedDocScript<E> extends DocScript<E> {
    final SealedDocScript<E> parent;
    final Language myLanguage;
    final CharSequence text;
    final long timeStamp;
    SyntaxTraverser<E> raw;
    JBIterable<SyntaxTraverser<E>> cached;

    SealedDocScript(Project project, VirtualFile file, Document document, @NotNull Language language) {
      super(project, file, document);
      this.text = document.getImmutableCharSequence();
      this.timeStamp = document.getModificationStamp();
      this.myLanguage = language;
      this.parent = null;
    }

    SealedDocScript(SealedDocScript<E> sealed, TextRange range) {
      super(sealed, range);
      this.text = sealed.text;
      this.timeStamp = sealed.timeStamp;
      this.myLanguage = sealed.myLanguage;
      this.parent = sealed;
    }

    @Override
    @NotNull
    public Language getLanguage() {
      return this.myLanguage;
    }

    @Override
    @NotNull
    public JBIterable<SyntaxTraverser<E>> getScript() {
      return this.cached != null ? this.cached : (this.cached = this.getScriptImpl());
    }

    @Override
    public DocScript<E> subScript(@Nullable TextRange range) {
      SealedDocScript<E> p2 = this.parent == null ? this : this.parent;
      return range == null ? p2 : new SealedDocScript<>(p2, range);
    }

    @NotNull
    SyntaxTraverser<E> parseRaw() {
      EvaluationHelper helper = new RedisEvaluationHelper();
      return this.parent != null ? this.parent.parseRaw() : (this.raw != null ? this.raw : (this.raw = helper.parse(this.project, this.myLanguage, this.text)));
    }

    @NotNull
    protected Function<SyntaxTraverser<E>, SyntaxTraverser<E>> toStatements() {
      EvaluationHelper helper = new RedisEvaluationHelper();
      return s -> helper.statements(this, s);
    }

    @NotNull
    JBIterable<SyntaxTraverser<E>> getScriptImpl() {
      boolean positionOrChosen = this.range instanceof ScriptModel.PositionRange || this.range instanceof ScriptModel.ChosenRange;
      boolean inRange = !positionOrChosen && this.range != null;
      this.raw = this.parseRaw();
      if (inRange) {
        JBIterable adjusted = (this.toStatements().fun(this.raw)).traverse();
        boolean singleBatch = true;
        int selectionOption = ScriptModelUtil.getSelectionOption(this.range);
        boolean justOne = selectionOption == 1 || selectionOption == 2 || adjusted.take(2).size() == 1 || singleBatch && adjusted.transform(this.raw.api.TO_RANGE).filter(Conditions.not(ScriptModelUtil.IN_RANGE(this.range))).isEmpty() || adjusted.transform(this.raw.api.TO_RANGE).filter(Conditions.not(ScriptModelUtil.CONTAINS_RANGE(this.range))).isEmpty();
        if (justOne) {
          Object first2;
          EvaluationHelper helper = new RedisEvaluationHelper();
          SyntaxTraverser exact = helper.parse(this.project, this.myLanguage, this.range.subSequence(this.text));
          exact.putUserData(ScriptModelUtil.PART_OFFSET, this.range.getStartOffset());
          if (selectionOption == 3 && adjusted.take(2).size() == 1 && ((first2 = exact.traverse().first()) == null || first2 instanceof PsiElement && ((PsiElement) first2).getTextRange().getStartOffset() != 0 || first2 instanceof LighterASTNode && ((LighterASTNode) first2).getStartOffset() != 0)) {
            JBIterable jBIterable = JBIterable.of(this.raw);
            return jBIterable;
          }
          JBIterable jBIterable = JBIterable.of((Object) exact);
          return jBIterable;
        }
      }
      JBIterable jBIterable = JBIterable.of(this.raw);
      return jBIterable;
    }
  }

  private static class TrackingDocScript<E> extends DocScript<E> {

    SealedDocScript<E> sealed;

    TrackingDocScript(Project project, VirtualFile file, Document document) {
      super(project, file, document);
    }

    @Override
    public DocScript<E> subScript(@Nullable TextRange range) {
      return this.getSealed().subScript(range);
    }

    @Override
    public JBIterable<SyntaxTraverser<E>> getScript() {
      return this.getSealed().getScript();
    }

    SealedDocScript<E> getSealed() {
      Language dialect = this.getLanguage();
      if (this.sealed == null || this.sealed.timeStamp != this.document.getModificationStamp() || this.sealed.myLanguage != dialect) {
        this.sealed = new SealedDocScript<>(this.project, this.vFile, this.document, dialect);
      }
      return this.sealed;
    }
  }

  private static abstract class DocScript<E> extends ScriptBase<E> {
    final Document document;

    DocScript(Project project, VirtualFile file, Document document) {
      super(project, file, null);
      this.document = document;
    }

    DocScript(DocScript<E> script, TextRange range) {
      super(script.project, script.vFile, range);
      this.document = script.document;
    }

    @Override
    public UserDataHolder getParamDataHolder() {
      return this.document;
    }
  }

  private static abstract class ScriptBase<V> implements Script<V> {

    final Project project;
    final VirtualFile vFile;
    final TextRange range;

    ScriptBase(Project project, VirtualFile vFile, TextRange range) {
      this.project = project;
      this.vFile = vFile;
      this.range = range;
    }

    @NotNull
    @Override
    public Language getLanguage() {
      return SqlScriptModel.getLanguage(this.vFile, this.project);
    }

    @Override
    public abstract ScriptBase<V> subScript(@Nullable TextRange textRange);

    @Override
    @Nullable
    public TextRange getRange() {
      return this.range;
    }

    @Override
    @NotNull
    public Project getProject() {
      return this.project;
    }

    @Override
    @NotNull
    public VirtualFile getVirtualFile() {
      return this.vFile;
    }
  }
}

