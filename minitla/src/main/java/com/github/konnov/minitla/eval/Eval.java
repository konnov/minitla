package com.github.konnov.minitla.eval;

import com.github.konnov.minitla.ir.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple expression evaluator, mainly, for double-checking the correctness of the solver.
 * The evaluator assumes that the input expressions are well-formed, that is, they are not ill-typed
 * and have the right number of children for each operator.
 *
 * @author Igor Konnov, 2024
 */
public class Eval {
    /**
     * Evaluation environment that maps names to values, e.g., assigned via `:set-const`.
     */
    private final Map<String, Expr> env = new HashMap<>();

    /**
     * Evaluate an expression by producing a literal, e.g., `BoolValueExpr`.
     * @param expr the expression to evaluate
     * @return the result of the evaluation that must be a literal
     */
    public Expr eval(Expr expr) {
        return switch (expr) {
            case BoolLitExpr b -> b;

            case NameExpr n -> {
                if (!env.containsKey(n.name())) {
                    throw new EvalError("No value for " + n.name());
                }
                yield env.get(n.name());
            }

            case OperatorExpr o -> evalOperator(o);

            default -> throw new IllegalArgumentException("Unexpected expression: " + expr);
        };
    }

    private Expr evalOperator(OperatorExpr expr) {
        return switch (expr.operator()) {
            case Operator.NOT -> {
                var arg = eval(expr.children()[0]);
                yield BoolLitExpr.of(!arg.asBoolLit().value());
            }

            case Operator.AND -> {
                var result = Arrays
                        .stream(expr.children())
                        .map(this::eval)
                        .allMatch(e -> e instanceof BoolLitExpr && e.asBoolLit().value());
                yield BoolLitExpr.of(result);
            }

            case Operator.OR -> {
                var result = Arrays
                        .stream(expr.children())
                        .map(this::eval)
                        .anyMatch(e -> e instanceof BoolLitExpr && e.asBoolLit().value());
                yield BoolLitExpr.of(result);
            }

            case Operator.IMPLIES -> {
                var arg0 = eval(expr.children()[0]);
                var arg1 = eval(expr.children()[1]);
                yield BoolLitExpr.of(!arg0.asBoolLit().value() || arg1.asBoolLit().value());
            }

            case Operator.IFF -> {
                var arg0 = eval(expr.children()[0]);
                var arg1 = eval(expr.children()[1]);
                yield BoolLitExpr.of(arg0.asBoolLit().value() == arg1.asBoolLit().value());
            }

            case Operator.CONST ->
                BoolLitExpr.of(true);

            case Operator.SET_CONST -> {
                var name = expr.children()[0].asName().name();
                var value = eval(expr.children()[1]);
                env.put(name, value);
                yield new BoolLitExpr(true);
            }
        };
    }
}
