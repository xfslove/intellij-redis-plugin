//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.xfslove.intellij.plugin.redis.script;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.util.TextRange;
import com.intellij.util.containers.JBIterable;
import org.jetbrains.annotations.Nullable;

public abstract class ScriptModel<E> implements Disposable {

  public abstract ScriptModel<E> subModel(@Nullable TextRange textRange);

  public abstract JBIterable<CommandIterator<E>> statements();

  public abstract TextRange getTextRange();

  @Override
  public void dispose() {
  }

}
