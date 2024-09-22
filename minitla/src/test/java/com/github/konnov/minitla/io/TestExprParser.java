package com.github.konnov.minitla.io;

import com.github.konnov.minitla.ir.BoolValueExpr;
import com.github.konnov.minitla.ir.Expr;
import com.github.konnov.minitla.ir.OperatorExpr;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for {@link ExprParser}.
 */
public class TestExprParser {
    @Test
    public void testParseEmpty() {
        var exprs = parse("");
        assert(exprs.isEmpty());
    }

    @Test
    public void testParseFalse() {
        var exprs = parse("false");
        assert(exprs.size() == 1);
        assert(exprs.get(0) instanceof BoolValueExpr && !((BoolValueExpr) exprs.get(0)).value());
    }

    @Test
    public void testParseTrue() {
        var exprs = parse("true");
        assert(exprs.size() == 1);
        assert(exprs.get(0) instanceof BoolValueExpr && ((BoolValueExpr) exprs.get(0)).value());
    }

    @Test
    public void testParseAnd() {
        var exprs = parse("(and false true)");
        assert(exprs.size() == 1);
        var top = (OperatorExpr) exprs.get(0);
        assert("and".equals(top.operator()));
        assert(top.children()[0] instanceof BoolValueExpr && !((BoolValueExpr) top.children()[0]).value());
        assert(top.children()[1] instanceof BoolValueExpr && ((BoolValueExpr) top.children()[1]).value());
    }

    @Test
    public void testParseOr() {
        var exprs = parse("(or false true)");
        assert(exprs.size() == 1);
        var top = (OperatorExpr) exprs.get(0);
        assert("or".equals(top.operator()));
        assert(top.children()[0] instanceof BoolValueExpr && !((BoolValueExpr) top.children()[0]).value());
        assert(top.children()[1] instanceof BoolValueExpr && ((BoolValueExpr) top.children()[1]).value());
    }

    @Test
    public void testFailOnEmptyParentheses() {
        try {
            parse("()");
            fail("Expected a syntax error");
        } catch (SyntaxError e) {
            // ok
        }
    }

    @Test
    public void testFailOnTooManyOpenParentheses() {
        try {
            parse("(((");
            fail("Expected a syntax error");
        } catch (SyntaxError e) {
            // ok
        }
    }

    @Test
    public void testFailOnInsufficientClosingParentheses() {
        try {
            parse("(((and false)");
            fail("Expected a syntax error");
        } catch (SyntaxError e) {
            // ok
        }
    }

    private ArrayList<Expr> parse(String text) {
        return new ExprParser("string", new StringReader(text)).parse();
    }
}

