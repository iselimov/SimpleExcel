package com.defrag.lexer;

public class Literal extends Token {

    private StringBuilder text = new StringBuilder();

    public Literal() {
        super(Type.LITERAL);
    }

    @Override
    public void addSymbol(char symbol) {
        text.append(symbol);
    }

    @Override
    public Object getValue() {
        return text.toString();
    }
}