// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.github.xfslove.intellij.plugin.redis.lang.psi;

import com.github.xfslove.intellij.plugin.redis.psi.RedisProperty;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;

public class RedisElementFactory {

  public static RedisProperty createProperty(Project project, String name) {
    final RedisFile file = createFile(project, name);
    return (RedisProperty) file.getFirstChild();
  }

  public static RedisFile createFile(Project project, String text) {
    String name = "dummy.redis";
    return (RedisFile) PsiFileFactory.getInstance( project).createFileFromText(name, RedisFileType.INSTANCE, text);
  }

  public static RedisProperty createProperty(Project project, String name, String value) {
    final RedisFile file = createFile(project, name + " = " + value);
    return (RedisProperty) file.getFirstChild();
  }

  public static PsiElement createCRLF(Project project) {
    final RedisFile file = createFile(project, "\n");
    return file.getFirstChild();
  }

}