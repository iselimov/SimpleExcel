package com.defrag.parser;

import lombok.Setter;

@Setter
public class OperationExpression extends Expression {

    private Expression left;
    private Expression right;

    public OperationExpression(Expression parent) {
        super(Type.OPERATOR, parent);
    }
}