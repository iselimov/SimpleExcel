package com.defrag.lexer;

import lombok.Getter;

@Getter
public class CellReference extends Token {

    private int rowIndex;

    private char colName;

    protected CellReference(char colName) {
        super(Type.REFERENCE);
        this.colName = Character.toUpperCase(colName);
    }

    @Override
    public void addSymbol(char symbol) {
        if (!Character.isDigit(symbol) || symbol == '0') {
            throw new LexerException(LexerException.Error.CELL_REF_FORMAT);
        }
        if (rowIndex > 0) {
            throw new LexerException(LexerException.Error.CELL_REF_FORMAT);
        }
        rowIndex = Character.digit(symbol, 10) - 1;
    }

    @Override
    public Object getValue() {
        return new int[] {rowIndex, 0};
    }
}