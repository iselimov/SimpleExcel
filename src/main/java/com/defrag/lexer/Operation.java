package com.defrag.lexer;

public class Operation extends Token {

    private final Type value;

    public Operation(Type value) {
        super(Token.Type.OPERATION);
        this.value = value;
    }

    @Override
    public void addSymbol(char symbol) {
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

        public final char val;
        Type(char val) {
            this.val = val;
        }

        public boolean isSuitable(char op) {
            return  val == op;
        }
    }
}