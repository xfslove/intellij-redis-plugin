// This is a generated file. Not intended for manual editing.
package com.github.xfslove.intellij.plugin.redis.lang;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.github.xfslove.intellij.plugin.redis.lang.RedisTypes.*;
import com.intellij.navigation.ItemPresentation;

public class RedisCommandImpl extends RedisNamedElementImpl implements RedisCommand {

  public RedisCommandImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull RedisVisitor visitor) {
    visitor.visitCommand(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof RedisVisitor) accept((RedisVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getKey() {
    return findNotNullChildByType(KEY);
  }

  @Override
  public String getName() {
    return RedisPsiImplUtil.getName(this);
  }

  @Override
  public PsiElement setName(String newName) {
    return RedisPsiImplUtil.setName(this, newName);
  }

  @Override
  public PsiElement getNameIdentifier() {
    return RedisPsiImplUtil.getNameIdentifier(this);
  }

  @Override
  public ItemPresentation getPresentation() {
    return RedisPsiImplUtil.getPresentation(this);
  }

}
