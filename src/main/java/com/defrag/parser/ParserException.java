package com.defrag.parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

class ParserException extends RuntimeException {

    @Getter private final Error error;
    @Getter private final Expression errNode;

    ParserException(Error error) {
        this(error, null);
    }

    ParserException(Error error, Expression errNode) {
        super("#" + error.getText());
        this.error = error;
        this.errNode = errNode;
    }

    @Getter
    @RequiredArgsConstructor
    enum Error {
        TOO_MANY_TOKENS("Was found more than one token"),
        TOKENS_NOT_FOUND("Tokens was not found"),
        CYCLE_WAS_FOUND("Was found a cycle"),
        OPERATION_INVALID_ARGS("Invalid operation args"),
        INTEGER_OVERFLOW("Integer overflow"),
        DIVIDING_BY_ZERO("Operation dividing by zero was found"),
        UNKNOWN_OPERATION("Unknown operation")

        ;

        private final String text;
    }
}