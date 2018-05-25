package com.defrag.lexer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class LexerException extends RuntimeException {

    public LexerException(Error error) {
        super(error.getText());
    }

    @Getter
    @RequiredArgsConstructor
    public enum Error {
        DIGIT_FORMAT("Неверный формат числа"),
        LITERAL_FORMAT("Символ ' может использоваться только в указании строковой константы"),
        CELL_REF_FORMAT("Неверный формат ссылки на ячейку"),
        UNKNOWN_FORMAT("Неизвестный формат ячейки")

        ;

        private final String text;
    }
}