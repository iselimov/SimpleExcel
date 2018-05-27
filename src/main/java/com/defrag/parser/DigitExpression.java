package com.defrag.parser;

public class DigitExpression extends Expression {

    private int value;

    public DigitExpression(Expression parent, int value) {
        super(Type.CONSTANT, parent);
        this.value = value;
    }

    @Override
    protected void collapse(Expression child) {
        child.getParent().collapse(child);
    }

    @Override
    protected Object getValue() {
        return value;
    }
}