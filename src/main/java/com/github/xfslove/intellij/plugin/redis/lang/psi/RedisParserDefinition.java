// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.github.xfslove.intellij.plugin.redis.lang.psi;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

public class RedisParserDefinition implements ParserDefinition {

  public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);
  public static final TokenSet COMMENTS = TokenSet.create(RedisTypes.COMMENT);
  
  public static final IFileElementType FILE = new IFileElementType(RedisLanguage.INSTANCE);
  
  @NotNull
  @Override
  public Lexer createLexer(Project project) {
    return new RedisLexerAdapter();
  }
  
  @NotNull
  @Override
  public TokenSet getWhitespaceTokens() {
    return WHITE_SPACES;
  }
  
  @NotNull
  @Override
  public TokenSet getCommentTokens() {
    return COMMENTS;
  }
  
  @NotNull
  @Override
  public TokenSet getStringLiteralElements() {
    return TokenSet.EMPTY;
  }
  
  @NotNull
  @Override
  public PsiParser createParser(final Project project) {
    return new RedisParser();
  }
  
  @Override
  public IFileElementType getFileNodeType() {
    return FILE;
  }
  
  @Override
  public PsiFile createFile(FileViewProvider viewProvider) {
    return new RedisFile(viewProvider);
  }
  
  @Override
  public SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
    return SpaceRequirements.MAY;
  }
  
  @NotNull
  @Override
  public PsiElement createElement(ASTNode node) {
    return RedisTypes.Factory.createElement(node);
  }
}