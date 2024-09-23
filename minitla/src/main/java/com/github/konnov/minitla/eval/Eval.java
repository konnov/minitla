package com.github.konnov.minitla.eval;

import com.github.konnov.minitla.ir.BoolLitExpr;
import com.github.konnov.minitla.ir.Expr;
import com.github.konnov.minitla.ir.NameExpr;
import com.github.konnov.minitla.ir.OperatorExpr;

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
            case "not" -> {
                var arg = eval(expr.children()[0]);
                yield BoolLitExpr.of(!arg.asBoolLit().value());
            }

            case "and" -> {
                var result = Arrays
                        .stream(expr.children())
                        .map(this::eval)
                        .allMatch(e -> e instanceof BoolLitExpr && e.asBoolLit().value());
                yield BoolLitExpr.of(result);
            }

            case "or" -> {
                var result = Arrays
                        .stream(expr.children())
                        .map(this::eval)
                        .anyMatch(e -> e instanceof BoolLitExpr && e.asBoolLit().value());
                yield BoolLitExpr.of(result);
            }

            case "implies" -> {
                var arg0 = eval(expr.children()[0]);
                var arg1 = eval(expr.children()[1]);
                yield BoolLitExpr.of(!arg0.asBoolLit().value() || arg1.asBoolLit().value());
            }

            case "iff" -> {
                var arg0 = eval(expr.children()[0]);
                var arg1 = eval(expr.children()[1]);
                yield BoolLitExpr.of(arg0.asBoolLit().value() == arg1.asBoolLit().value());
            }

            case ":const" ->
                BoolLitExpr.of(true);

            case ":set-const" -> {
                var name = expr.children()[0].asName().name();
                var value = eval(expr.children()[1]);
                env.put(name, value);
                yield new BoolLitExpr(true);
            }

            default ->
                throw new EvalError("Unexpected operator: " + expr.operator());
        };
    }
}
