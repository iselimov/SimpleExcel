package com.defrag.lexer;

import static com.defrag.lexer.LexerException.Error.DIGIT_FORMAT;

/**
 * Non negative integer number non terminal representation
 */
class Digit extends Token {

    private final int cellIndex;
    private final StringBuilder digit = new StringBuilder();

    Digit(int cellIndex, char firstSymbol) {
        super(Type.DIGIT);
        this.cellIndex = cellIndex;
        digit.append(Character.digit(firstSymbol, 10));
    }

    @Override
    void addSymbol(char symbol) {
        if (!Character.isDigit(symbol)) {
            throw new LexerException(DIGIT_FORMAT, cellIndex);
        }
        digit.append(Character.digit(symbol, 10));
    }

    @Override
    public Object getValue() {
        try {
            return Integer.valueOf(digit.toString());
        } catch (NumberFormatException e) {
            throw new LexerException(DIGIT_FORMAT, cellIndex);
        }
    }
}