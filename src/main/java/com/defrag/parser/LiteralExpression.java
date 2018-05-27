package com.defrag.parser;

public class LiteralExpression extends Expression {

    private final String text;

    public LiteralExpression(Expression parent, String text) {
        super(Type.LITERAL, parent);
        this.text = text;
    }

    @Override
    protected void collapse(Expression child) {
        child.getParent().collapse(child);
    }

    @Override
    protected Object getValue() {
        return text;
    }
}