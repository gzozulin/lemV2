// Generated from Statements.g4 by ANTLR 4.7.2

package com.gzozulin.statements;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class StatementsParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		DelimitedComment=1, LineComment=2, Any=3;
	public static final int
		RULE_statements = 0, RULE_delimitedComment = 1, RULE_lineComment = 2, 
		RULE_code = 3;
	private static String[] makeRuleNames() {
		return new String[] {
				"com/gzozulin/statements", "delimitedComment", "lineComment", "code"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "DelimitedComment", "LineComment", "Any"
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
	public String getGrammarFileName() { return "Statements.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public StatementsParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class StatementsContext extends ParserRuleContext {
		public List<DelimitedCommentContext> delimitedComment() {
			return getRuleContexts(DelimitedCommentContext.class);
		}
		public DelimitedCommentContext delimitedComment(int i) {
			return getRuleContext(DelimitedCommentContext.class,i);
		}
		public List<LineCommentContext> lineComment() {
			return getRuleContexts(LineCommentContext.class);
		}
		public LineCommentContext lineComment(int i) {
			return getRuleContext(LineCommentContext.class,i);
		}
		public List<CodeContext> code() {
			return getRuleContexts(CodeContext.class);
		}
		public CodeContext code(int i) {
			return getRuleContext(CodeContext.class,i);
		}
		public StatementsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statements; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).enterStatements(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).exitStatements(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StatementsVisitor ) return ((StatementsVisitor<? extends T>)visitor).visitStatements(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementsContext statements() throws RecognitionException {
		StatementsContext _localctx = new StatementsContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_statements);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(13);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DelimitedComment) | (1L << LineComment) | (1L << Any))) != 0)) {
				{
				setState(11);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case DelimitedComment:
					{
					setState(8);
					delimitedComment();
					}
					break;
				case LineComment:
					{
					setState(9);
					lineComment();
					}
					break;
				case Any:
					{
					setState(10);
					code();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(15);
				_errHandler.sync(this);
				_la = _input.LA(1);
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

	public static class DelimitedCommentContext extends ParserRuleContext {
		public TerminalNode DelimitedComment() { return getToken(StatementsParser.DelimitedComment, 0); }
		public DelimitedCommentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_delimitedComment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).enterDelimitedComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).exitDelimitedComment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StatementsVisitor ) return ((StatementsVisitor<? extends T>)visitor).visitDelimitedComment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DelimitedCommentContext delimitedComment() throws RecognitionException {
		DelimitedCommentContext _localctx = new DelimitedCommentContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_delimitedComment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(16);
			match(DelimitedComment);
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

	public static class LineCommentContext extends ParserRuleContext {
		public TerminalNode LineComment() { return getToken(StatementsParser.LineComment, 0); }
		public LineCommentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lineComment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).enterLineComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).exitLineComment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StatementsVisitor ) return ((StatementsVisitor<? extends T>)visitor).visitLineComment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LineCommentContext lineComment() throws RecognitionException {
		LineCommentContext _localctx = new LineCommentContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_lineComment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(18);
			match(LineComment);
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

	public static class CodeContext extends ParserRuleContext {
		public List<TerminalNode> Any() { return getTokens(StatementsParser.Any); }
		public TerminalNode Any(int i) {
			return getToken(StatementsParser.Any, i);
		}
		public CodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_code; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).enterCode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).exitCode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StatementsVisitor ) return ((StatementsVisitor<? extends T>)visitor).visitCode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CodeContext code() throws RecognitionException {
		CodeContext _localctx = new CodeContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_code);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(21); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(20);
					match(Any);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(23); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\5\34\4\2\t\2\4\3"+
		"\t\3\4\4\t\4\4\5\t\5\3\2\3\2\3\2\7\2\16\n\2\f\2\16\2\21\13\2\3\3\3\3\3"+
		"\4\3\4\3\5\6\5\30\n\5\r\5\16\5\31\3\5\2\2\6\2\4\6\b\2\2\2\33\2\17\3\2"+
		"\2\2\4\22\3\2\2\2\6\24\3\2\2\2\b\27\3\2\2\2\n\16\5\4\3\2\13\16\5\6\4\2"+
		"\f\16\5\b\5\2\r\n\3\2\2\2\r\13\3\2\2\2\r\f\3\2\2\2\16\21\3\2\2\2\17\r"+
		"\3\2\2\2\17\20\3\2\2\2\20\3\3\2\2\2\21\17\3\2\2\2\22\23\7\3\2\2\23\5\3"+
		"\2\2\2\24\25\7\4\2\2\25\7\3\2\2\2\26\30\7\5\2\2\27\26\3\2\2\2\30\31\3"+
		"\2\2\2\31\27\3\2\2\2\31\32\3\2\2\2\32\t\3\2\2\2\5\r\17\31";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}