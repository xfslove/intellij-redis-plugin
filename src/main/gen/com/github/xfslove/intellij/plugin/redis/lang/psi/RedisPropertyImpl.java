// This is a generated file. Not intended for manual editing.
package com.github.xfslove.intellij.plugin.redis.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.github.xfslove.intellij.plugin.redis.lang.psi.RedisTypes.*;
import com.intellij.navigation.ItemPresentation;

public class RedisPropertyImpl extends RedisNamedElementImpl implements RedisProperty {

  public RedisPropertyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull RedisVisitor visitor) {
    visitor.visitProperty(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof RedisVisitor) accept((RedisVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getCommand() {
    return findNotNullChildByType(COMMAND);
  }

  @Override
  @Nullable
  public PsiElement getField() {
    return findChildByType(FIELD);
  }

  @Override
  @Nullable
  public PsiElement getValue() {
    return findChildByType(VALUE);
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
