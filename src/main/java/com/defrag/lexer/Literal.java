package com.defrag.lexer;

class Literal extends Token {

    private StringBuilder text = new StringBuilder();

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