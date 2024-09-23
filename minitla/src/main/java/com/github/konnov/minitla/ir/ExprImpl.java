package com.github.konnov.minitla.ir;

public abstract class ExprImpl implements Expr {
    public BoolLitExpr asBoolLit() {
        return (BoolLitExpr) this;
    }

    public NameExpr asName() {
        return (NameExpr) this;
    }

    public OperatorExpr asOperator() {
        return (OperatorExpr) this;
    }
}
