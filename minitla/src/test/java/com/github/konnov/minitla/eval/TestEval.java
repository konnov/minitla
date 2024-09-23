package com.github.konnov.minitla.eval;

import com.github.konnov.minitla.ir.BoolLitExpr;
import com.github.konnov.minitla.ir.NameExpr;
import com.github.konnov.minitla.ir.OperatorExpr;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for [[Eval]].
 */
public class TestEval {
    private final BoolLitExpr FALSE = BoolLitExpr.of(false);
    private final BoolLitExpr TRUE = BoolLitExpr.of(true);

    @Test
    public void testEvalFalse() {
        var result = new Eval().eval(FALSE);
        assert(!result.asBoolLit().value());
    }

    @Test
    public void testEvalTrue() {
        var result = new Eval().eval(TRUE);
        assert(result.asBoolLit().value());
    }

    @Test
    public void testEvalNot() {
        var expr = OperatorExpr.of("not", FALSE);
        var result = new Eval().eval(expr);
        assert(result.asBoolLit().value());
    }

    @Test
    public void testEvalAnd() {
        var expr = OperatorExpr.of("and", TRUE, FALSE, TRUE);
        var result = new Eval().eval(expr);
        assert(!result.asBoolLit().value());
    }

    @Test
    public void testEvalOr() {
        var expr = OperatorExpr.of("or", TRUE, FALSE, TRUE);
        var result = new Eval().eval(expr);
        assert(result.asBoolLit().value());
    }

    @Test
    public void testEvalImplies() {
        var expr = OperatorExpr.of("implies", FALSE, TRUE);
        var result = new Eval().eval(expr);
        assert(result.asBoolLit().value());
    }

    @Test
    public void testEvalIff() {
        var expr = OperatorExpr.of("iff", FALSE, TRUE);
        var result = new Eval().eval(expr);
        assert(!result.asBoolLit().value());
    }

    @Test
    public void testEvalNameAfterSetConst() {
        var eval = new Eval();
        var x = NameExpr.of("x");
        eval.eval(OperatorExpr.of(":const", x, NameExpr.of("Bool")));
        eval.eval(OperatorExpr.of(":set-const", x, TRUE));
        var expr = OperatorExpr.of("or", FALSE, x);
        var result = eval.eval(expr);
        assert(result.asBoolLit().value());
    }

    @Test
    public void testEvalUndefinedConst() {
        var expr = NameExpr.of("x");
        try {
            new Eval().eval(expr);
            fail("Expected EvalError");
        } catch (EvalError e) {
            // ok
        }
    }
}
