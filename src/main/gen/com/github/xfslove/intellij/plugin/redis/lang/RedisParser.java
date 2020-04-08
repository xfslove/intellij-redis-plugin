// This is a generated file. Not intended for manual editing.
package com.github.xfslove.intellij.plugin.redis.lang;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.github.xfslove.intellij.plugin.redis.lang.RedisTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class RedisParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return redisPluginFile(b, l + 1);
  }

  /* ********************************************************** */
  // KEY FIELD FIELD?
  public static boolean command(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, COMMAND, "<command>");
    r = consumeTokens(b, 1, KEY, FIELD);
    p = r; // pin = 1
    r = r && command_2(b, l + 1);
    exit_section_(b, l, m, r, p, recover_command_parser_);
    return r || p;
  }

  // FIELD?
  private static boolean command_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_2")) return false;
    consumeToken(b, FIELD);
    return true;
  }

  /* ********************************************************** */
  // command|COMMENT
  static boolean command_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_")) return false;
    if (!nextTokenIs(b, "", COMMENT, KEY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = command(b, l + 1);
    if (!r) r = consumeToken(b, COMMENT);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(KEY|COMMENT)
  static boolean recover_command(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_command")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_command_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // KEY|COMMENT
  private static boolean recover_command_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_command_0")) return false;
    boolean r;
    r = consumeToken(b, KEY);
    if (!r) r = consumeToken(b, COMMENT);
    return r;
  }

  /* ********************************************************** */
  // command_*
  static boolean redisPluginFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "redisPluginFile")) return false;
    while (true) {
      int c = current_position_(b);
      if (!command_(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "redisPluginFile", c)) break;
    }
    return true;
  }

  static final Parser recover_command_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return recover_command(b, l + 1);
    }
  };
}
