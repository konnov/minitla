package com.github.konnov.minitla.ir;

/**
 * Just a name expression, e.g., `x`. The name normally corresponds to a constant.
 * @author Igor Konnov, 2024
 */
public class NameExpr extends ExprImpl {
    private final String name;

    public NameExpr(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public static NameExpr of(String name) {
        return new NameExpr(name);
    }
}
