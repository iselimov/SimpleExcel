package com.defrag.parser;

/**
 * Literal node of AST
 */
class LiteralExpression extends Expression {

    private final String text;

    LiteralExpression(Expression parent, String text) {
        super(Type.LITERAL, parent);
        this.text = text;
    }

    @Override
    void collapse(Expression child) {
        child.getParent().collapse(child);
    }

    @Override
    Object getValue() {
        return text;
    }
}