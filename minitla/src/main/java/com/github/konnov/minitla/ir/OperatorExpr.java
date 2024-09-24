package com.github.konnov.minitla.ir;

/**
 * An expression that is constructed via an operator, e.g., `(and false true)`.
 * @author Igor Konnov, 2024
 */
public class OperatorExpr extends ExprImpl {
    private final Operator operator;
    private final Expr[] children;

    /**
     * Construct an operator expression.
     * @param operator the operator name, currently, just a string for convenience
     * @param children the array of children expressions (must be immutable!)
     */
    public OperatorExpr(Operator operator, Expr[] children) {
        this.operator = operator;
        this.children = children;
    }

    public Operator operator() {
        return operator;
    }

    public Expr[] children() {
        return children;
    }

    public static OperatorExpr of(Operator operator, Expr... children) {
        return new OperatorExpr(operator, children);
    }
}