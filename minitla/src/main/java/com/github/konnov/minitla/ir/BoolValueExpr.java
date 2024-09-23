package com.github.konnov.minitla.ir;

/**
 * A Boolean value: false or true.
 * @param value the Boolean value
 * @author Igor Konnov, 2024
 */
public record BoolValueExpr(Boolean value) implements Expr {
}
