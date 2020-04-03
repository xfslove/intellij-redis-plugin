// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.github.xfslove.intellij.plugin.redis.lang.psi;

import com.github.xfslove.intellij.plugin.redis.RedisIcon;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class RedisPsiImplUtil {

  public static String getCommand(RedisProperty element) {
    ASTNode keyNode = element.getNode().findChildByType(RedisTypes.COMMAND);
    if (keyNode != null) {
      // IMPORTANT: Convert embedded escaped spaces to simple spaces
      return keyNode.getText().replaceAll("\\\\ ", " ");
    } else {
      return null;
    }
  }

  public static String getField(RedisProperty element) {
    ASTNode valueNode = element.getNode().findChildByType(RedisTypes.FIELD);
    if (valueNode != null) {
      return valueNode.getText();
    } else {
      return null;
    }
  }

  public static String getValue(RedisProperty element) {
    ASTNode valueNode = element.getNode().findChildByType(RedisTypes.VALUE);
    if (valueNode != null) {
      return valueNode.getText();
    } else {
      return null;
    }
  }

  public static String getName(RedisProperty element) {
    return getCommand(element);
  }

  public static PsiElement setName(RedisProperty element, String newName) {
    ASTNode keyNode = element.getNode().findChildByType(RedisTypes.COMMAND);
    if (keyNode != null) {
      RedisProperty property = RedisElementFactory.createProperty(element.getProject(), newName);
      ASTNode newKeyNode = property.getFirstChild().getNode();
      element.getNode().replaceChild(keyNode, newKeyNode);
    }
    return element;
  }

  public static PsiElement getNameIdentifier(RedisProperty element) {
    ASTNode keyNode = element.getNode().findChildByType(RedisTypes.COMMAND);
    if (keyNode != null) {
      return keyNode.getPsi();
    } else {
      return null;
    }
  }

  public static ItemPresentation getPresentation(final RedisProperty element) {
    return new ItemPresentation() {
      @Nullable
      @Override
      public String getPresentableText() {
        return element.getName();
      }

      @Nullable
      @Override
      public String getLocationString() {
        PsiFile containingFile = element.getContainingFile();
        return containingFile == null ? null : containingFile.getName();
      }

      @Override
      public Icon getIcon(boolean unused) {
        return RedisIcon.get();
      }
    };
  }

}