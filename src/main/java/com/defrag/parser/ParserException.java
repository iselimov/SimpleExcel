package com.defrag.parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

class ParserException extends RuntimeException {

    @Getter private final Error error;
    @Getter private final CellReferenceExpression errNode;

    ParserException(Error error) {
        this(error, null);
    }

    ParserException(Error error, CellReferenceExpression errNode) {
        super("#" + error.getText());
        this.error = error;
        this.errNode = errNode;
    }

    @Getter
    @RequiredArgsConstructor
    enum Error {
        TOO_MANY_TOKENS("Найдено больше одного токена"),
        TOKENS_NOT_FOUND("Не найдено ни одного токена"),
        CYCLE_WAS_FOUND("Был найден цикл"),
        OPERATION_INVALID_ARGS("Невалидные аргументы операции"),
        INTEGER_OVERFLOW("Переполнение целочисленного типа"),
        DIVIDING_BY_ZERO("Обнаружена операция деления на ноль"),
        UNKNOWN_OPERATION("Неизвестная операция")

        ;

        private final String text;
    }
}