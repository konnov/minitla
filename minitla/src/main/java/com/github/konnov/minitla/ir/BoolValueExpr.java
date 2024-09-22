package com.github.konnov.minitla.ir;

/**
 * A Boolean value: false or true.
 * @param value the Boolean value
 */
public record BoolValueExpr(Boolean value) implements Expr {
}
