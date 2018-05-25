package com.defrag.lexer;

import lombok.Getter;

public abstract class Token {

    public enum Type {
        REFERENCE,
        DIGIT,
        OPERATION,
        LITERAL
    }

    @Getter
    private final Type type;

    protected Token(Type type) {
        this.type = type;
    }

    public abstract void addSymbol(char symbol);

    public abstract Object getValue();
}