package com.github.konnov.minitla.io;

import com.github.konnov.minitla.ir.Expr;
import com.github.konnov.minitla.ir.Operator;
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
        assert(!exprs.getFirst().asBoolLit().value());
    }

    @Test
    public void testParseTrue() {
        var exprs = parse("true");
        assert(exprs.size() == 1);
        assert(exprs.getFirst().asBoolLit().value());
    }

    @Test
    public void testParseName() {
        var exprs = parse("x");
        assert(exprs.size() == 1);
        assert(exprs.getFirst().asName().name().equals("x"));
    }

    @Test
    public void testParseAnd() {
        var exprs = parse("(and false true)");
        assert(exprs.size() == 1);
        var top = (OperatorExpr) exprs.getFirst();
        assert(Operator.AND.equals(top.operator()));
        assert(!top.children()[0].asBoolLit().value());
        assert(top.children()[1].asBoolLit().value());
    }

    @Test
    public void testParseOr() {
        var exprs = parse("(or false true)");
        assert(exprs.size() == 1);
        var top = (OperatorExpr) exprs.getFirst();
        assert(Operator.OR.equals(top.operator()));
        assert(!top.children()[0].asBoolLit().value());
        assert(top.children()[1].asBoolLit().value());
    }

    @Test
    public void testParseConst() {
        var exprs = parse("(:const x Bool)");
        assert(exprs.size() == 1);
        var top = (OperatorExpr) exprs.getFirst();
        assert(Operator.CONST.equals(top.operator()));
        assert(top.children()[0].asName().name().equals("x"));
        assert(top.children()[1].asName().name().equals("Bool"));
    }

    @Test
    public void testParseSetConst() {
        var exprs = parse("(:set-const x true)");
        assert(exprs.size() == 1);
        var top = (OperatorExpr) exprs.getFirst();
        assert(Operator.SET_CONST.equals(top.operator()));
        assert(top.children()[0].asName().name().equals("x"));
        assert(top.children()[1].asBoolLit().value());
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

    @Test
    public void testFailOnUnexpectedDirective() {
        try {
            parse("(:unexpected true)");
            fail("Expected a syntax error");
        } catch (SyntaxError e) {
            // ok
        }
    }

    private ArrayList<Expr> parse(String text) {
        return new ExprParser("string", new StringReader(text)).parse();
    }
}

