package com.defrag.lexer;

import lombok.Getter;

@Getter
public class CellReference extends Token {

    private final char colName;
    private final int cellIndex;
    private final int rowsSize;
    private final int colSize;
    private int rowIndex = -1;
    private int colIndex;

    CellReference(int cellIndex, char colName, int rowsSize, int colsSize) {
        super(Type.REFERENCE);
        this.cellIndex = cellIndex;
        this.rowsSize = rowsSize;
        this.colSize = colsSize;
        this.colName = Character.toUpperCase(colName);
        colIndex = colName - 65;
        if (colIndex < 0 || colIndex > 25 || colIndex >= colSize) {
            throw new LexerException(LexerException.Error.CELL_REF_FORMAT, cellIndex);
        }
    }

    @Override
    public void addSymbol(char symbol) {
        if (!Character.isDigit(symbol) || symbol == '0') {
            throw new LexerException(LexerException.Error.CELL_REF_FORMAT, cellIndex);
        }
        if (rowIndex > 0) {
            throw new LexerException(LexerException.Error.CELL_REF_FORMAT, cellIndex);
        }
        rowIndex = Character.digit(symbol, 10) - 1;
        if (rowIndex >= rowsSize) {
            throw new LexerException(LexerException.Error.CELL_REF_FORMAT, cellIndex);
        }
    }

    @Override
    public Object getValue() {
        if (rowIndex == -1) {
            throw new LexerException(LexerException.Error.CELL_REF_FORMAT, cellIndex);
        }
        return rowIndex * colSize + colIndex;
    }
}