package com.defrag.lexer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class LexerException extends RuntimeException {

    @Getter private final int cellIndex;

    LexerException(Error error, int cellIndex) {
        super("#" + error.getText());
        this.cellIndex = cellIndex;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Error {
        DIGIT_FORMAT("Wring digit format"),
        LITERAL_FORMAT("Symbol ' can use only with string constant"),
        CELL_REF_FORMAT("Wrong format reference to cell"),
        UNKNOWN_FORMAT("Unknown cell format")

        ;

        private final String text;
    }
}