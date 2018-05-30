package com.defrag.parser;

/**
 * Digit node of AST
 */
class DigitExpression extends Expression {

    private int value;

    DigitExpression(Expression parent, int value) {
        super(Type.CONSTANT, parent);
        this.value = value;
    }

    @Override
    void collapse(Expression child) {
        child.getParent().collapse(child);
    }

    @Override
    Object getValue() {
        return value;
    }
}