package com.github.konnov.minitla.ir;

/**
 * A Boolean literal: `false` or `true`.
 * @author Igor Konnov, 2024
 */
public class BoolLitExpr extends ExprImpl {
    private final Boolean value;

    public BoolLitExpr(Boolean value) {
        this.value = value;
    }

    public Boolean value() {
        return value;
    }

    /**
     * Creates a new Boolean value expression.
     * @param value the Boolean value
     * @return the created expression
     */
    public static BoolLitExpr of(boolean value) {
        return new BoolLitExpr(value);
    }
}
