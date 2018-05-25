package com.defrag.parser;

import com.defrag.Cell;

public class DigitExpression extends Expression {

    private int value;

    public DigitExpression(Cell cell, int value) {
        this(null, cell, value);
    }

    public DigitExpression(Expression parent, Cell cell, int value) {
        super(Type.CONSTANT, parent);
        this.value = value;
    }
}