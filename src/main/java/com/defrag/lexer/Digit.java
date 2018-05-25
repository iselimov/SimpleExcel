package com.defrag.lexer;

import static com.defrag.lexer.LexerException.Error.DIGIT_FORMAT;

public class Digit extends Token {

    private StringBuilder digit = new StringBuilder();

    public Digit(char firstSymbol) {
        super(Type.DIGIT);
        digit.append(Character.digit(firstSymbol, 10));
    }

    @Override
    public void addSymbol(char symbol) {
        if (!Character.isDigit(symbol)) {
            throw new LexerException(DIGIT_FORMAT);
        }
        digit.append(Character.digit(symbol, 10));
    }

    @Override
    public Object getValue() {
        return Integer.valueOf(digit.toString());
    }
}