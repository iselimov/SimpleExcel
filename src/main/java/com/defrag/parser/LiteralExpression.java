package com.defrag.parser;

import com.defrag.Cell;

public class LiteralExpression extends Expression {

    private final String text;

    public LiteralExpression(Cell cell, String text) {
        this(null, cell, text);
    }

    public LiteralExpression(Expression parent, Cell cell, String text) {
        super(Type.LITERAL);
        this.text = text;
    }
}