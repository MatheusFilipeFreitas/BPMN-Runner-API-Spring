// Generated from ProcessParser.g4 by ANTLR 4.13.1

package com.matheusfilipefreitas.bpmn_runner_api.model.script.grammar;

import java.util.List;

import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class ProcessParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		POOL=1, PROCESS=2, START=3, TASK=4, END=5, GATEWAY=6, SCOPE=7, YES=8, 
		NO=9, MESSAGE=10, TaskType=11, GatewayType=12, ID=13, STRING=14, LPAREN=15, 
		RPAREN=16, LBRACE=17, RBRACE=18, COMMA=19, SEMICOLON=20, ARROW=21, WS=22, 
		LINE_COMMENT=23, BLOCK_COMMENT=24;
	public static final int
		RULE_model = 0, RULE_pool = 1, RULE_process = 2, RULE_flowElement = 3, 
		RULE_taskRule = 4, RULE_startRule = 5, RULE_endRule = 6, RULE_gatewayRule = 7, 
		RULE_parallelScope = 8, RULE_exclusiveScope = 9, RULE_yesBranch = 10, 
		RULE_noBranch = 11, RULE_taskRef = 12, RULE_messageRef = 13;
	private static String[] makeRuleNames() {
		return new String[] {
			"model", "pool", "process", "flowElement", "taskRule", "startRule", "endRule", 
			"gatewayRule", "parallelScope", "exclusiveScope", "yesBranch", "noBranch", 
			"taskRef", "messageRef"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'pool'", "'process'", "'start'", "'task'", "'end'", "'gateway'", 
			"'scope'", "'yes'", "'no'", "'message'", null, null, null, null, "'('", 
			"')'", "'{'", "'}'", "','", "';'", "'->'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "POOL", "PROCESS", "START", "TASK", "END", "GATEWAY", "SCOPE", 
			"YES", "NO", "MESSAGE", "TaskType", "GatewayType", "ID", "STRING", "LPAREN", 
			"RPAREN", "LBRACE", "RBRACE", "COMMA", "SEMICOLON", "ARROW", "WS", "LINE_COMMENT", 
			"BLOCK_COMMENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "ProcessParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ProcessParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ModelContext extends ParserRuleContext {
		public List<PoolContext> pool() {
			return getRuleContexts(PoolContext.class);
		}
		public PoolContext pool(int i) {
			return getRuleContext(PoolContext.class,i);
		}
		public ModelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_model; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).enterModel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).exitModel(this);
		}
	}

	public final ModelContext model() throws RecognitionException {
		ModelContext _localctx = new ModelContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_model);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(29); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(28);
				pool();
				}
				}
				setState(31); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==POOL );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PoolContext extends ParserRuleContext {
		public TerminalNode POOL() { return getToken(ProcessParser.POOL, 0); }
		public TerminalNode LPAREN() { return getToken(ProcessParser.LPAREN, 0); }
		public TerminalNode ID() { return getToken(ProcessParser.ID, 0); }
		public TerminalNode COMMA() { return getToken(ProcessParser.COMMA, 0); }
		public TerminalNode STRING() { return getToken(ProcessParser.STRING, 0); }
		public TerminalNode RPAREN() { return getToken(ProcessParser.RPAREN, 0); }
		public TerminalNode LBRACE() { return getToken(ProcessParser.LBRACE, 0); }
		public ProcessContext process() {
			return getRuleContext(ProcessContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(ProcessParser.RBRACE, 0); }
		public PoolContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pool; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).enterPool(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).exitPool(this);
		}
	}

	public final PoolContext pool() throws RecognitionException {
		PoolContext _localctx = new PoolContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_pool);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(33);
			match(POOL);
			setState(34);
			match(LPAREN);
			setState(35);
			match(ID);
			setState(36);
			match(COMMA);
			setState(37);
			match(STRING);
			setState(38);
			match(RPAREN);
			setState(39);
			match(LBRACE);
			setState(40);
			process();
			setState(41);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProcessContext extends ParserRuleContext {
		public TerminalNode PROCESS() { return getToken(ProcessParser.PROCESS, 0); }
		public TerminalNode LPAREN() { return getToken(ProcessParser.LPAREN, 0); }
		public TerminalNode ID() { return getToken(ProcessParser.ID, 0); }
		public TerminalNode COMMA() { return getToken(ProcessParser.COMMA, 0); }
		public TerminalNode STRING() { return getToken(ProcessParser.STRING, 0); }
		public TerminalNode RPAREN() { return getToken(ProcessParser.RPAREN, 0); }
		public TerminalNode LBRACE() { return getToken(ProcessParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(ProcessParser.RBRACE, 0); }
		public StartRuleContext startRule() {
			return getRuleContext(StartRuleContext.class,0);
		}
		public List<FlowElementContext> flowElement() {
			return getRuleContexts(FlowElementContext.class);
		}
		public FlowElementContext flowElement(int i) {
			return getRuleContext(FlowElementContext.class,i);
		}
		public List<EndRuleContext> endRule() {
			return getRuleContexts(EndRuleContext.class);
		}
		public EndRuleContext endRule(int i) {
			return getRuleContext(EndRuleContext.class,i);
		}
		public ProcessContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_process; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).enterProcess(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).exitProcess(this);
		}
	}

	public final ProcessContext process() throws RecognitionException {
		ProcessContext _localctx = new ProcessContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_process);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(43);
			match(PROCESS);
			setState(44);
			match(LPAREN);
			setState(45);
			match(ID);
			setState(46);
			match(COMMA);
			setState(47);
			match(STRING);
			setState(48);
			match(RPAREN);
			setState(49);
			match(LBRACE);
			setState(51);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==START) {
				{
				setState(50);
				startRule();
				}
			}

			setState(56);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(53);
					flowElement();
					}
					} 
				}
				setState(58);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			setState(62);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==END) {
				{
				{
				setState(59);
				endRule();
				}
				}
				setState(64);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(65);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FlowElementContext extends ParserRuleContext {
		public TaskRefContext taskRef() {
			return getRuleContext(TaskRefContext.class,0);
		}
		public GatewayRuleContext gatewayRule() {
			return getRuleContext(GatewayRuleContext.class,0);
		}
		public EndRuleContext endRule() {
			return getRuleContext(EndRuleContext.class,0);
		}
		public FlowElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_flowElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).enterFlowElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).exitFlowElement(this);
		}
	}

	public final FlowElementContext flowElement() throws RecognitionException {
		FlowElementContext _localctx = new FlowElementContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_flowElement);
		try {
			setState(70);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(67);
				taskRef();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(68);
				gatewayRule();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(69);
				endRule();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TaskRuleContext extends ParserRuleContext {
		public TerminalNode TASK() { return getToken(ProcessParser.TASK, 0); }
		public TerminalNode LPAREN() { return getToken(ProcessParser.LPAREN, 0); }
		public TerminalNode ID() { return getToken(ProcessParser.ID, 0); }
		public List<TerminalNode> COMMA() { return getTokens(ProcessParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ProcessParser.COMMA, i);
		}
		public TerminalNode STRING() { return getToken(ProcessParser.STRING, 0); }
		public TerminalNode TaskType() { return getToken(ProcessParser.TaskType, 0); }
		public TerminalNode RPAREN() { return getToken(ProcessParser.RPAREN, 0); }
		public TaskRuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_taskRule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).enterTaskRule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).exitTaskRule(this);
		}
	}

	public final TaskRuleContext taskRule() throws RecognitionException {
		TaskRuleContext _localctx = new TaskRuleContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_taskRule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			match(TASK);
			setState(73);
			match(LPAREN);
			setState(74);
			match(ID);
			setState(75);
			match(COMMA);
			setState(76);
			match(STRING);
			setState(77);
			match(COMMA);
			setState(78);
			match(TaskType);
			setState(79);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StartRuleContext extends ParserRuleContext {
		public TerminalNode START() { return getToken(ProcessParser.START, 0); }
		public TerminalNode LPAREN() { return getToken(ProcessParser.LPAREN, 0); }
		public TerminalNode ID() { return getToken(ProcessParser.ID, 0); }
		public TerminalNode RPAREN() { return getToken(ProcessParser.RPAREN, 0); }
		public TerminalNode SEMICOLON() { return getToken(ProcessParser.SEMICOLON, 0); }
		public StartRuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_startRule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).enterStartRule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).exitStartRule(this);
		}
	}

	public final StartRuleContext startRule() throws RecognitionException {
		StartRuleContext _localctx = new StartRuleContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_startRule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(81);
			match(START);
			setState(82);
			match(LPAREN);
			setState(83);
			match(ID);
			setState(84);
			match(RPAREN);
			setState(85);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EndRuleContext extends ParserRuleContext {
		public TerminalNode END() { return getToken(ProcessParser.END, 0); }
		public TerminalNode LPAREN() { return getToken(ProcessParser.LPAREN, 0); }
		public TerminalNode ID() { return getToken(ProcessParser.ID, 0); }
		public TerminalNode RPAREN() { return getToken(ProcessParser.RPAREN, 0); }
		public TerminalNode SEMICOLON() { return getToken(ProcessParser.SEMICOLON, 0); }
		public EndRuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_endRule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).enterEndRule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).exitEndRule(this);
		}
	}

	public final EndRuleContext endRule() throws RecognitionException {
		EndRuleContext _localctx = new EndRuleContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_endRule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(87);
			match(END);
			setState(88);
			match(LPAREN);
			setState(89);
			match(ID);
			setState(90);
			match(RPAREN);
			setState(91);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class GatewayRuleContext extends ParserRuleContext {
		public TerminalNode GATEWAY() { return getToken(ProcessParser.GATEWAY, 0); }
		public TerminalNode LPAREN() { return getToken(ProcessParser.LPAREN, 0); }
		public TerminalNode ID() { return getToken(ProcessParser.ID, 0); }
		public List<TerminalNode> COMMA() { return getTokens(ProcessParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ProcessParser.COMMA, i);
		}
		public TerminalNode STRING() { return getToken(ProcessParser.STRING, 0); }
		public TerminalNode GatewayType() { return getToken(ProcessParser.GatewayType, 0); }
		public TerminalNode RPAREN() { return getToken(ProcessParser.RPAREN, 0); }
		public TerminalNode LBRACE() { return getToken(ProcessParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(ProcessParser.RBRACE, 0); }
		public TerminalNode SEMICOLON() { return getToken(ProcessParser.SEMICOLON, 0); }
		public ExclusiveScopeContext exclusiveScope() {
			return getRuleContext(ExclusiveScopeContext.class,0);
		}
		public List<ParallelScopeContext> parallelScope() {
			return getRuleContexts(ParallelScopeContext.class);
		}
		public ParallelScopeContext parallelScope(int i) {
			return getRuleContext(ParallelScopeContext.class,i);
		}
		public GatewayRuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_gatewayRule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).enterGatewayRule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).exitGatewayRule(this);
		}
	}

	public final GatewayRuleContext gatewayRule() throws RecognitionException {
		GatewayRuleContext _localctx = new GatewayRuleContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_gatewayRule);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(93);
			match(GATEWAY);
			setState(94);
			match(LPAREN);
			setState(95);
			match(ID);
			setState(96);
			match(COMMA);
			setState(97);
			match(STRING);
			setState(98);
			match(COMMA);
			setState(99);
			match(GatewayType);
			setState(100);
			match(RPAREN);
			setState(101);
			match(LBRACE);
			setState(108);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SCOPE:
				{
				setState(103); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(102);
					parallelScope();
					}
					}
					setState(105); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==SCOPE );
				}
				break;
			case YES:
				{
				setState(107);
				exclusiveScope();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(110);
			match(RBRACE);
			setState(111);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParallelScopeContext extends ParserRuleContext {
		public TerminalNode SCOPE() { return getToken(ProcessParser.SCOPE, 0); }
		public TerminalNode ARROW() { return getToken(ProcessParser.ARROW, 0); }
		public TerminalNode LBRACE() { return getToken(ProcessParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(ProcessParser.RBRACE, 0); }
		public List<FlowElementContext> flowElement() {
			return getRuleContexts(FlowElementContext.class);
		}
		public FlowElementContext flowElement(int i) {
			return getRuleContext(FlowElementContext.class,i);
		}
		public ParallelScopeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parallelScope; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).enterParallelScope(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).exitParallelScope(this);
		}
	}

	public final ParallelScopeContext parallelScope() throws RecognitionException {
		ParallelScopeContext _localctx = new ParallelScopeContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_parallelScope);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(113);
			match(SCOPE);
			setState(114);
			match(ARROW);
			setState(115);
			match(LBRACE);
			setState(117); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(116);
				flowElement();
				}
				}
				setState(119); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 8304L) != 0) );
			setState(121);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExclusiveScopeContext extends ParserRuleContext {
		public YesBranchContext yesBranch() {
			return getRuleContext(YesBranchContext.class,0);
		}
		public NoBranchContext noBranch() {
			return getRuleContext(NoBranchContext.class,0);
		}
		public ExclusiveScopeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exclusiveScope; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).enterExclusiveScope(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).exitExclusiveScope(this);
		}
	}

	public final ExclusiveScopeContext exclusiveScope() throws RecognitionException {
		ExclusiveScopeContext _localctx = new ExclusiveScopeContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_exclusiveScope);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(123);
			yesBranch();
			setState(124);
			noBranch();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class YesBranchContext extends ParserRuleContext {
		public TerminalNode YES() { return getToken(ProcessParser.YES, 0); }
		public TerminalNode ARROW() { return getToken(ProcessParser.ARROW, 0); }
		public List<FlowElementContext> flowElement() {
			return getRuleContexts(FlowElementContext.class);
		}
		public FlowElementContext flowElement(int i) {
			return getRuleContext(FlowElementContext.class,i);
		}
		public TerminalNode LBRACE() { return getToken(ProcessParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(ProcessParser.RBRACE, 0); }
		public YesBranchContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_yesBranch; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).enterYesBranch(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).exitYesBranch(this);
		}
	}

	public final YesBranchContext yesBranch() throws RecognitionException {
		YesBranchContext _localctx = new YesBranchContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_yesBranch);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(126);
			match(YES);
			setState(127);
			match(ARROW);
			setState(137);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TASK:
			case END:
			case GATEWAY:
			case ID:
				{
				setState(128);
				flowElement();
				}
				break;
			case LBRACE:
				{
				setState(129);
				match(LBRACE);
				setState(131); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(130);
					flowElement();
					}
					}
					setState(133); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 8304L) != 0) );
				setState(135);
				match(RBRACE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NoBranchContext extends ParserRuleContext {
		public TerminalNode NO() { return getToken(ProcessParser.NO, 0); }
		public TerminalNode ARROW() { return getToken(ProcessParser.ARROW, 0); }
		public List<FlowElementContext> flowElement() {
			return getRuleContexts(FlowElementContext.class);
		}
		public FlowElementContext flowElement(int i) {
			return getRuleContext(FlowElementContext.class,i);
		}
		public TerminalNode LBRACE() { return getToken(ProcessParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(ProcessParser.RBRACE, 0); }
		public NoBranchContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_noBranch; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).enterNoBranch(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).exitNoBranch(this);
		}
	}

	public final NoBranchContext noBranch() throws RecognitionException {
		NoBranchContext _localctx = new NoBranchContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_noBranch);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(139);
			match(NO);
			setState(140);
			match(ARROW);
			setState(150);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TASK:
			case END:
			case GATEWAY:
			case ID:
				{
				setState(141);
				flowElement();
				}
				break;
			case LBRACE:
				{
				setState(142);
				match(LBRACE);
				setState(144); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(143);
					flowElement();
					}
					}
					setState(146); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 8304L) != 0) );
				setState(148);
				match(RBRACE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TaskRefContext extends ParserRuleContext {
		public TaskRuleContext taskRule() {
			return getRuleContext(TaskRuleContext.class,0);
		}
		public TerminalNode SEMICOLON() { return getToken(ProcessParser.SEMICOLON, 0); }
		public TerminalNode ARROW() { return getToken(ProcessParser.ARROW, 0); }
		public MessageRefContext messageRef() {
			return getRuleContext(MessageRefContext.class,0);
		}
		public GatewayRuleContext gatewayRule() {
			return getRuleContext(GatewayRuleContext.class,0);
		}
		public TerminalNode ID() { return getToken(ProcessParser.ID, 0); }
		public TaskRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_taskRef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).enterTaskRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).exitTaskRef(this);
		}
	}

	public final TaskRefContext taskRef() throws RecognitionException {
		TaskRefContext _localctx = new TaskRefContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_taskRef);
		int _la;
		try {
			setState(166);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TASK:
				enterOuterAlt(_localctx, 1);
				{
				setState(152);
				taskRule();
				setState(155);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ARROW) {
					{
					setState(153);
					match(ARROW);
					setState(154);
					messageRef();
					}
				}

				setState(157);
				match(SEMICOLON);
				}
				break;
			case GATEWAY:
				enterOuterAlt(_localctx, 2);
				{
				setState(159);
				gatewayRule();
				}
				break;
			case ID:
				enterOuterAlt(_localctx, 3);
				{
				setState(160);
				match(ID);
				setState(163);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ARROW) {
					{
					setState(161);
					match(ARROW);
					setState(162);
					messageRef();
					}
				}

				setState(165);
				match(SEMICOLON);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MessageRefContext extends ParserRuleContext {
		public TerminalNode MESSAGE() { return getToken(ProcessParser.MESSAGE, 0); }
		public TerminalNode LPAREN() { return getToken(ProcessParser.LPAREN, 0); }
		public TerminalNode ID() { return getToken(ProcessParser.ID, 0); }
		public TerminalNode RPAREN() { return getToken(ProcessParser.RPAREN, 0); }
		public MessageRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_messageRef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).enterMessageRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProcessParserListener ) ((ProcessParserListener)listener).exitMessageRef(this);
		}
	}

	public final MessageRefContext messageRef() throws RecognitionException {
		MessageRefContext _localctx = new MessageRefContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_messageRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(168);
			match(MESSAGE);
			setState(169);
			match(LPAREN);
			setState(170);
			match(ID);
			setState(171);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u0018\u00ae\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
		"\u0002\f\u0007\f\u0002\r\u0007\r\u0001\u0000\u0004\u0000\u001e\b\u0000"+
		"\u000b\u0000\f\u0000\u001f\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0003\u00024\b\u0002\u0001\u0002\u0005\u0002"+
		"7\b\u0002\n\u0002\f\u0002:\t\u0002\u0001\u0002\u0005\u0002=\b\u0002\n"+
		"\u0002\f\u0002@\t\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0003\u0003G\b\u0003\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0004\u0007h\b\u0007"+
		"\u000b\u0007\f\u0007i\u0001\u0007\u0003\u0007m\b\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0004\bv\b\b\u000b"+
		"\b\f\bw\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0004\n\u0084\b\n\u000b\n\f\n\u0085\u0001\n\u0001\n"+
		"\u0003\n\u008a\b\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0004\u000b\u0091\b\u000b\u000b\u000b\f\u000b\u0092\u0001\u000b"+
		"\u0001\u000b\u0003\u000b\u0097\b\u000b\u0001\f\u0001\f\u0001\f\u0003\f"+
		"\u009c\b\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0003\f\u00a4"+
		"\b\f\u0001\f\u0003\f\u00a7\b\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r"+
		"\u0001\r\u0000\u0000\u000e\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012"+
		"\u0014\u0016\u0018\u001a\u0000\u0000\u00b0\u0000\u001d\u0001\u0000\u0000"+
		"\u0000\u0002!\u0001\u0000\u0000\u0000\u0004+\u0001\u0000\u0000\u0000\u0006"+
		"F\u0001\u0000\u0000\u0000\bH\u0001\u0000\u0000\u0000\nQ\u0001\u0000\u0000"+
		"\u0000\fW\u0001\u0000\u0000\u0000\u000e]\u0001\u0000\u0000\u0000\u0010"+
		"q\u0001\u0000\u0000\u0000\u0012{\u0001\u0000\u0000\u0000\u0014~\u0001"+
		"\u0000\u0000\u0000\u0016\u008b\u0001\u0000\u0000\u0000\u0018\u00a6\u0001"+
		"\u0000\u0000\u0000\u001a\u00a8\u0001\u0000\u0000\u0000\u001c\u001e\u0003"+
		"\u0002\u0001\u0000\u001d\u001c\u0001\u0000\u0000\u0000\u001e\u001f\u0001"+
		"\u0000\u0000\u0000\u001f\u001d\u0001\u0000\u0000\u0000\u001f \u0001\u0000"+
		"\u0000\u0000 \u0001\u0001\u0000\u0000\u0000!\"\u0005\u0001\u0000\u0000"+
		"\"#\u0005\u000f\u0000\u0000#$\u0005\r\u0000\u0000$%\u0005\u0013\u0000"+
		"\u0000%&\u0005\u000e\u0000\u0000&\'\u0005\u0010\u0000\u0000\'(\u0005\u0011"+
		"\u0000\u0000()\u0003\u0004\u0002\u0000)*\u0005\u0012\u0000\u0000*\u0003"+
		"\u0001\u0000\u0000\u0000+,\u0005\u0002\u0000\u0000,-\u0005\u000f\u0000"+
		"\u0000-.\u0005\r\u0000\u0000./\u0005\u0013\u0000\u0000/0\u0005\u000e\u0000"+
		"\u000001\u0005\u0010\u0000\u000013\u0005\u0011\u0000\u000024\u0003\n\u0005"+
		"\u000032\u0001\u0000\u0000\u000034\u0001\u0000\u0000\u000048\u0001\u0000"+
		"\u0000\u000057\u0003\u0006\u0003\u000065\u0001\u0000\u0000\u00007:\u0001"+
		"\u0000\u0000\u000086\u0001\u0000\u0000\u000089\u0001\u0000\u0000\u0000"+
		"9>\u0001\u0000\u0000\u0000:8\u0001\u0000\u0000\u0000;=\u0003\f\u0006\u0000"+
		"<;\u0001\u0000\u0000\u0000=@\u0001\u0000\u0000\u0000><\u0001\u0000\u0000"+
		"\u0000>?\u0001\u0000\u0000\u0000?A\u0001\u0000\u0000\u0000@>\u0001\u0000"+
		"\u0000\u0000AB\u0005\u0012\u0000\u0000B\u0005\u0001\u0000\u0000\u0000"+
		"CG\u0003\u0018\f\u0000DG\u0003\u000e\u0007\u0000EG\u0003\f\u0006\u0000"+
		"FC\u0001\u0000\u0000\u0000FD\u0001\u0000\u0000\u0000FE\u0001\u0000\u0000"+
		"\u0000G\u0007\u0001\u0000\u0000\u0000HI\u0005\u0004\u0000\u0000IJ\u0005"+
		"\u000f\u0000\u0000JK\u0005\r\u0000\u0000KL\u0005\u0013\u0000\u0000LM\u0005"+
		"\u000e\u0000\u0000MN\u0005\u0013\u0000\u0000NO\u0005\u000b\u0000\u0000"+
		"OP\u0005\u0010\u0000\u0000P\t\u0001\u0000\u0000\u0000QR\u0005\u0003\u0000"+
		"\u0000RS\u0005\u000f\u0000\u0000ST\u0005\r\u0000\u0000TU\u0005\u0010\u0000"+
		"\u0000UV\u0005\u0014\u0000\u0000V\u000b\u0001\u0000\u0000\u0000WX\u0005"+
		"\u0005\u0000\u0000XY\u0005\u000f\u0000\u0000YZ\u0005\r\u0000\u0000Z[\u0005"+
		"\u0010\u0000\u0000[\\\u0005\u0014\u0000\u0000\\\r\u0001\u0000\u0000\u0000"+
		"]^\u0005\u0006\u0000\u0000^_\u0005\u000f\u0000\u0000_`\u0005\r\u0000\u0000"+
		"`a\u0005\u0013\u0000\u0000ab\u0005\u000e\u0000\u0000bc\u0005\u0013\u0000"+
		"\u0000cd\u0005\f\u0000\u0000de\u0005\u0010\u0000\u0000el\u0005\u0011\u0000"+
		"\u0000fh\u0003\u0010\b\u0000gf\u0001\u0000\u0000\u0000hi\u0001\u0000\u0000"+
		"\u0000ig\u0001\u0000\u0000\u0000ij\u0001\u0000\u0000\u0000jm\u0001\u0000"+
		"\u0000\u0000km\u0003\u0012\t\u0000lg\u0001\u0000\u0000\u0000lk\u0001\u0000"+
		"\u0000\u0000mn\u0001\u0000\u0000\u0000no\u0005\u0012\u0000\u0000op\u0005"+
		"\u0014\u0000\u0000p\u000f\u0001\u0000\u0000\u0000qr\u0005\u0007\u0000"+
		"\u0000rs\u0005\u0015\u0000\u0000su\u0005\u0011\u0000\u0000tv\u0003\u0006"+
		"\u0003\u0000ut\u0001\u0000\u0000\u0000vw\u0001\u0000\u0000\u0000wu\u0001"+
		"\u0000\u0000\u0000wx\u0001\u0000\u0000\u0000xy\u0001\u0000\u0000\u0000"+
		"yz\u0005\u0012\u0000\u0000z\u0011\u0001\u0000\u0000\u0000{|\u0003\u0014"+
		"\n\u0000|}\u0003\u0016\u000b\u0000}\u0013\u0001\u0000\u0000\u0000~\u007f"+
		"\u0005\b\u0000\u0000\u007f\u0089\u0005\u0015\u0000\u0000\u0080\u008a\u0003"+
		"\u0006\u0003\u0000\u0081\u0083\u0005\u0011\u0000\u0000\u0082\u0084\u0003"+
		"\u0006\u0003\u0000\u0083\u0082\u0001\u0000\u0000\u0000\u0084\u0085\u0001"+
		"\u0000\u0000\u0000\u0085\u0083\u0001\u0000\u0000\u0000\u0085\u0086\u0001"+
		"\u0000\u0000\u0000\u0086\u0087\u0001\u0000\u0000\u0000\u0087\u0088\u0005"+
		"\u0012\u0000\u0000\u0088\u008a\u0001\u0000\u0000\u0000\u0089\u0080\u0001"+
		"\u0000\u0000\u0000\u0089\u0081\u0001\u0000\u0000\u0000\u008a\u0015\u0001"+
		"\u0000\u0000\u0000\u008b\u008c\u0005\t\u0000\u0000\u008c\u0096\u0005\u0015"+
		"\u0000\u0000\u008d\u0097\u0003\u0006\u0003\u0000\u008e\u0090\u0005\u0011"+
		"\u0000\u0000\u008f\u0091\u0003\u0006\u0003\u0000\u0090\u008f\u0001\u0000"+
		"\u0000\u0000\u0091\u0092\u0001\u0000\u0000\u0000\u0092\u0090\u0001\u0000"+
		"\u0000\u0000\u0092\u0093\u0001\u0000\u0000\u0000\u0093\u0094\u0001\u0000"+
		"\u0000\u0000\u0094\u0095\u0005\u0012\u0000\u0000\u0095\u0097\u0001\u0000"+
		"\u0000\u0000\u0096\u008d\u0001\u0000\u0000\u0000\u0096\u008e\u0001\u0000"+
		"\u0000\u0000\u0097\u0017\u0001\u0000\u0000\u0000\u0098\u009b\u0003\b\u0004"+
		"\u0000\u0099\u009a\u0005\u0015\u0000\u0000\u009a\u009c\u0003\u001a\r\u0000"+
		"\u009b\u0099\u0001\u0000\u0000\u0000\u009b\u009c\u0001\u0000\u0000\u0000"+
		"\u009c\u009d\u0001\u0000\u0000\u0000\u009d\u009e\u0005\u0014\u0000\u0000"+
		"\u009e\u00a7\u0001\u0000\u0000\u0000\u009f\u00a7\u0003\u000e\u0007\u0000"+
		"\u00a0\u00a3\u0005\r\u0000\u0000\u00a1\u00a2\u0005\u0015\u0000\u0000\u00a2"+
		"\u00a4\u0003\u001a\r\u0000\u00a3\u00a1\u0001\u0000\u0000\u0000\u00a3\u00a4"+
		"\u0001\u0000\u0000\u0000\u00a4\u00a5\u0001\u0000\u0000\u0000\u00a5\u00a7"+
		"\u0005\u0014\u0000\u0000\u00a6\u0098\u0001\u0000\u0000\u0000\u00a6\u009f"+
		"\u0001\u0000\u0000\u0000\u00a6\u00a0\u0001\u0000\u0000\u0000\u00a7\u0019"+
		"\u0001\u0000\u0000\u0000\u00a8\u00a9\u0005\n\u0000\u0000\u00a9\u00aa\u0005"+
		"\u000f\u0000\u0000\u00aa\u00ab\u0005\r\u0000\u0000\u00ab\u00ac\u0005\u0010"+
		"\u0000\u0000\u00ac\u001b\u0001\u0000\u0000\u0000\u000f\u001f38>Filw\u0085"+
		"\u0089\u0092\u0096\u009b\u00a3\u00a6";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}