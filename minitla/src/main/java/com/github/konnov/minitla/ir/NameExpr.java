package com.github.konnov.minitla.ir;

/**
 * Just a name expression, e.g., `x`. The name normally corresponds to a constant.
 * @param name the name carried by this expression
 * @author Igor Konnov, 2024
 */
public record NameExpr(String name) implements Expr {
}
