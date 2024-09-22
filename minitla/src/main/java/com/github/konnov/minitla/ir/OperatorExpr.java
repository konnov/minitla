package com.github.konnov.minitla.ir;

/**
 * An expression that is constructed via an operator, e.g., `(and false true)`.
 * @param operator the operator name, currently, just a string for convenience
 * @param children the array of children expressions (must be immutable!)
 */
public record OperatorExpr(String operator, Expr[] children) implements Expr {
}