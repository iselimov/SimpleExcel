package com.defrag.lexer;

/**
 * Text non terminal representation
 */
class Literal extends Token {

    private final StringBuilder text = new StringBuilder();

    Literal() {
        super(Type.LITERAL);
    }

    @Override
    void addSymbol(char symbol) {
        text.append(symbol);
    }

    @Override
    public Object getValue() {
        return text.toString();
    }
}