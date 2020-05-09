// Generated from Statements.g4 by ANTLR 4.7.2

package com.gzozulin.statements;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link StatementsParser}.
 */
public interface StatementsListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link StatementsParser#statements}.
	 * @param ctx the parse tree
	 */
	void enterStatements(StatementsParser.StatementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementsParser#statements}.
	 * @param ctx the parse tree
	 */
	void exitStatements(StatementsParser.StatementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementsParser#delimitedComment}.
	 * @param ctx the parse tree
	 */
	void enterDelimitedComment(StatementsParser.DelimitedCommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementsParser#delimitedComment}.
	 * @param ctx the parse tree
	 */
	void exitDelimitedComment(StatementsParser.DelimitedCommentContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementsParser#lineComment}.
	 * @param ctx the parse tree
	 */
	void enterLineComment(StatementsParser.LineCommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementsParser#lineComment}.
	 * @param ctx the parse tree
	 */
	void exitLineComment(StatementsParser.LineCommentContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementsParser#code}.
	 * @param ctx the parse tree
	 */
	void enterCode(StatementsParser.CodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementsParser#code}.
	 * @param ctx the parse tree
	 */
	void exitCode(StatementsParser.CodeContext ctx);
}