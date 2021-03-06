package com.defrag.lexer;

import lombok.Getter;

public abstract class Token {

    @Getter private final Type type;

    Token(Type type) {
        this.type = type;
    }

    abstract void addSymbol(char symbol);

    public abstract Object getValue();

    public enum Type {
        REFERENCE,
        DIGIT,
        OPERATION,
        LITERAL
    }
}