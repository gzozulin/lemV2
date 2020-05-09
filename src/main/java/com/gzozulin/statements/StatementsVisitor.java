// Generated from Statements.g4 by ANTLR 4.7.2

package com.gzozulin.statements;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link StatementsParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface StatementsVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link StatementsParser#statements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatements(StatementsParser.StatementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementsParser#delimitedComment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDelimitedComment(StatementsParser.DelimitedCommentContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementsParser#lineComment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLineComment(StatementsParser.LineCommentContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementsParser#code}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCode(StatementsParser.CodeContext ctx);
}