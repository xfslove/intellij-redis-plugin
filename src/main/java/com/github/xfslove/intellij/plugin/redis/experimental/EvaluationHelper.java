//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.xfslove.intellij.plugin.redis.experimental;

import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.SyntaxTraverser;
import org.jetbrains.annotations.NotNull;

public interface EvaluationHelper<E> {

  Condition<E> isStatement();

  SyntaxTraverser<E> statements(Script<E> script, SyntaxTraverser<E> syntaxTraverser);

  @NotNull
  SyntaxTraverser<E> parse(@NotNull Project project, @NotNull Language dialect, @NotNull CharSequence charSequence);

  @NotNull
  default SyntaxTraverser<E> getNotebookTraverser(@NotNull SyntaxTraverser<E> traverser) {
    return traverser;
  }
}
