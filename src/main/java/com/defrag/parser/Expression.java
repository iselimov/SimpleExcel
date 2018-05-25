package com.defrag.parser;

import lombok.Getter;

public abstract class Expression {

    @Getter
    private final Type type;
    private Expression parent;

    enum Type {
        CELL_REFERENCE,
        CONSTANT,
        OPERATOR,
        LITERAL
    }

    public Expression(Type type) {
        this.type = type;
    }

    protected Expression(Type type, Expression parent) {
        this.type = type;
        this.parent = parent;
    }

//    protected abstract void traverse();
}