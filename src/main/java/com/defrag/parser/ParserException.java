package com.defrag.parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ParserException extends RuntimeException {

    public ParserException(Error error) {
        super(error.getText());
    }

    @Getter
    @RequiredArgsConstructor
    public enum Error {

        TOO_MANY_TOKENS("Найдено больше одного токена"),
        TOKENS_NOT_FOUND("Не найдено ни одного токена"),
        CYCLE_WAS_FOUND("Был найден цикл"),
        OPERATION_INVALID_ARGS("Невалидные аргументы операции"),
        UNKNOWN_OPERATION("Неизвестная операция")

        ;

        private final String text;
    }
}