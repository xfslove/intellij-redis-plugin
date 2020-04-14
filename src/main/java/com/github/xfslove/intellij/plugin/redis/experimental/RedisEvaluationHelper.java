package com.github.xfslove.intellij.plugin.redis.experimental;

import com.github.xfslove.intellij.plugin.redis.lang.RedisCommand;
import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.Conditions;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.testFramework.ReadOnlyLightVirtualFile;
import com.intellij.util.containers.TreeTraversal;
import org.jetbrains.annotations.NotNull;

public final class RedisEvaluationHelper<E> implements EvaluationHelper<E> {

  @NotNull
  @Override
  public SyntaxTraverser<E> parse(@NotNull Project project, @NotNull Language dialect, @NotNull CharSequence documentText) {
    ReadOnlyLightVirtualFile file = new ReadOnlyLightVirtualFile("dummy.redis", dialect, documentText);
    SingleRootFileViewProvider.doNotCheckFileSizeLimit(file);
    PsiManager psiManager = PsiManager.getInstance(project);
    SingleRootFileViewProvider viewProvider = new SingleRootFileViewProvider(psiManager, file, false);
    PsiFile psiFile = viewProvider.getPsi(dialect);
    return (SyntaxTraverser<E>) SyntaxTraverser.psiTraverser(psiFile).expand(Conditions.alwaysFalse()).withTraversal(TreeTraversal.LEAVES_DFS);
  }

  @Override
  public Condition<E> isStatement() {
    return Conditions.instanceOf(RedisCommand.class);
  }

  @Override
  public SyntaxTraverser<E> statements(Script<E> script, SyntaxTraverser<E> syntaxTraverser) {

    final TextRange range = script.getRange();
    if (range == null) {
      return syntaxTraverser;
    }
//    else {
//
//      boolean positionOrChosen = range instanceof ScriptModel.PositionRange || range instanceof ScriptModel.ChosenRange;
//      final int offset = ScriptModelUtil.getPartOffsetAsInt(syntaxTraverser);
//
//      boolean leavesOnly = !positionOrChosen && range.getLength() > 0;
//      Condition var10000 = ScriptModelUtil.wholeFileCondition(syntaxTraverser, range, (new Condition() {
//        // $FF: synthetic method
//        // $FF: bridge method
//        public boolean value(Object var1) {
//          return this.value((IElementType)var1);
//        }
//
//        public final boolean value(IElementType type) {
//          return MongoEvaluationHelper.this.WS_AND_COMMENTS.contains(type);
//        }
//      }), this.isStatement());
//      Condition unparsedFileFilter = var10000;
//      var10000 = this.isStatement();
//      Condition var10001 = this.isType(this.EXPRESSIONS);
//      SyntaxTraverser.Api var10003 = syntaxTraverser.api;
//      var10000 = Conditions.or2(var10000, Conditions.and2(var10001, Conditions.not(this.isParentType(var10003, this.STATEMENTS))));
//      Condition filter = var10000;
//      var10000 = Conditions.or2(filter, unparsedFileFilter);
//      final Condition resultFilter = var10000;
//      FilteredTraverserBase var12 = (((syntaxTraverser.reset()).filter(resultFilter)).regard(filter)).regard((new FilteredTraverserBase.EdgeFilter() {
//        public boolean value(@NotNull LighterASTNode v) {
//          LighterASTNode var10000 = this.edgeSource != null && resultFilter.value(this.edgeSource) ? (LighterASTNode)this.edgeSource : null;
//          if (var10000 != null) {
//            LighterASTNode parent = var10000;
//            TextRange var5 = var10003.rangeOf(v);
//            TextRange vr = var5;
//            var5 = var10003.rangeOf(parent);
//            TextRange pr = var5;
//            return range.contains(pr.shiftRight(offset)) ? false : Intrinsics.areEqual(pr, vr) ^ true;
//          } else {
//            return true;
//          }
//        }
//
//        // $FF: synthetic method
//        // $FF: bridge method
//        public boolean value(Object var1) {
//          return this.value((LighterASTNode)var1);
//        }
//      }));
//      SyntaxTraverser adjusted = (SyntaxTraverser)var12;
//      SyntaxTraverser var13 = ScriptModelUtil.inRange(adjusted, range);
//      SyntaxTraverser ranged = var13;
//      var12 = ranged.withTraversal(leavesOnly ? TreeTraversal.LEAVES_DFS : TreeTraversal.PRE_ORDER_DFS);
//      return (SyntaxTraverser)var12;
//    }

    return null;

  }

}
