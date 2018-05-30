package com.defrag.lexer;

/**
 * Operation terminal representation
 */
public class Operation extends Token {

    private final Type value;

    Operation(Type value) {
        super(Token.Type.OPERATION);
        this.value = value;
    }

    @Override
    void addSymbol(char symbol) {
        // do nothing here
    }

    @Override
    public Object getValue() {
        return value;
    }

    public enum Type {
        PLUS('+'),
        SUBTRACT('-'),
        MULTIPLY('*'),
        DIVIDE('/');

        private final char val;
        Type(char val) {
            this.val = val;
        }

        public boolean isSuitable(char op) {
            return val == op;
        }
    }
}