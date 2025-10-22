// Generated from ProcessParser.g4 by ANTLR 4.13.1

package com.matheusfilipefreitas.bpmn_runner_api.model.script.grammar;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ProcessParser}.
 */
public interface ProcessParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ProcessParser#model}.
	 * @param ctx the parse tree
	 */
	void enterModel(ProcessParser.ModelContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProcessParser#model}.
	 * @param ctx the parse tree
	 */
	void exitModel(ProcessParser.ModelContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProcessParser#pool}.
	 * @param ctx the parse tree
	 */
	void enterPool(ProcessParser.PoolContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProcessParser#pool}.
	 * @param ctx the parse tree
	 */
	void exitPool(ProcessParser.PoolContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProcessParser#process}.
	 * @param ctx the parse tree
	 */
	void enterProcess(ProcessParser.ProcessContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProcessParser#process}.
	 * @param ctx the parse tree
	 */
	void exitProcess(ProcessParser.ProcessContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProcessParser#flowElement}.
	 * @param ctx the parse tree
	 */
	void enterFlowElement(ProcessParser.FlowElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProcessParser#flowElement}.
	 * @param ctx the parse tree
	 */
	void exitFlowElement(ProcessParser.FlowElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProcessParser#taskRule}.
	 * @param ctx the parse tree
	 */
	void enterTaskRule(ProcessParser.TaskRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProcessParser#taskRule}.
	 * @param ctx the parse tree
	 */
	void exitTaskRule(ProcessParser.TaskRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProcessParser#startRule}.
	 * @param ctx the parse tree
	 */
	void enterStartRule(ProcessParser.StartRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProcessParser#startRule}.
	 * @param ctx the parse tree
	 */
	void exitStartRule(ProcessParser.StartRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProcessParser#endRule}.
	 * @param ctx the parse tree
	 */
	void enterEndRule(ProcessParser.EndRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProcessParser#endRule}.
	 * @param ctx the parse tree
	 */
	void exitEndRule(ProcessParser.EndRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProcessParser#gatewayRule}.
	 * @param ctx the parse tree
	 */
	void enterGatewayRule(ProcessParser.GatewayRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProcessParser#gatewayRule}.
	 * @param ctx the parse tree
	 */
	void exitGatewayRule(ProcessParser.GatewayRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProcessParser#parallelScope}.
	 * @param ctx the parse tree
	 */
	void enterParallelScope(ProcessParser.ParallelScopeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProcessParser#parallelScope}.
	 * @param ctx the parse tree
	 */
	void exitParallelScope(ProcessParser.ParallelScopeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProcessParser#exclusiveScope}.
	 * @param ctx the parse tree
	 */
	void enterExclusiveScope(ProcessParser.ExclusiveScopeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProcessParser#exclusiveScope}.
	 * @param ctx the parse tree
	 */
	void exitExclusiveScope(ProcessParser.ExclusiveScopeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProcessParser#yesBranch}.
	 * @param ctx the parse tree
	 */
	void enterYesBranch(ProcessParser.YesBranchContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProcessParser#yesBranch}.
	 * @param ctx the parse tree
	 */
	void exitYesBranch(ProcessParser.YesBranchContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProcessParser#noBranch}.
	 * @param ctx the parse tree
	 */
	void enterNoBranch(ProcessParser.NoBranchContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProcessParser#noBranch}.
	 * @param ctx the parse tree
	 */
	void exitNoBranch(ProcessParser.NoBranchContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProcessParser#taskRef}.
	 * @param ctx the parse tree
	 */
	void enterTaskRef(ProcessParser.TaskRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProcessParser#taskRef}.
	 * @param ctx the parse tree
	 */
	void exitTaskRef(ProcessParser.TaskRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProcessParser#messageRef}.
	 * @param ctx the parse tree
	 */
	void enterMessageRef(ProcessParser.MessageRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProcessParser#messageRef}.
	 * @param ctx the parse tree
	 */
	void exitMessageRef(ProcessParser.MessageRefContext ctx);
}