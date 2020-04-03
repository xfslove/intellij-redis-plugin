package com.github.xfslove.intellij.plugin.redis.lang.psi;

import com.intellij.codeInsight.completion.*;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * @author wongiven
 * @date created at 2020/3/31
 */
public class RedisCompletionContributor extends CompletionContributor {

  public RedisCompletionContributor() {
    extend(CompletionType.BASIC, null, new CompletionProvider<CompletionParameters>() {
      @Override
      protected void addCompletions(@NotNull CompletionParameters parameters,
                                    @NotNull ProcessingContext context,
                                    @NotNull CompletionResultSet result) {

      }
    });
  }
}
