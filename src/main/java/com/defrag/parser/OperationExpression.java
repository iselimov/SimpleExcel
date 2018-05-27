package com.defrag.parser;

import com.defrag.lexer.Operation;
import lombok.Getter;
import lombok.Setter;

import java.util.function.BiFunction;
import java.util.stream.Stream;

public class OperationExpression extends Expression {

    private final OperationType operationType;
    @Getter @Setter private Expression left;
    private int leftVal = -1;
    @Getter @Setter private Expression right;
    private int rightVal = -1;
    private int value;

    public OperationExpression(Expression parent, Operation.Type tokenOpType) {
        super(Type.OPERATOR, parent);
        operationType = OperationType.define(tokenOpType);
    }

    public void collapse(Expression child) {
        if (child.getType() == Type.LITERAL) {
            throw new ParserException(ParserException.Error.OPERATION_INVALID_ARGS);
        }
        if (child.getType() == Type.CELL_REFERENCE) {
            Object childValue = child.getValue();
            if (childValue instanceof String) {
                throw new ParserException(ParserException.Error.OPERATION_INVALID_ARGS);
            }
        }
        if (child == left) {
            leftVal = (int) child.getValue();
            if (rightVal == -1) {
                leftLeaf(right).ifPresent(next -> next.collapse(next));
            }
        } else if (child == right) {
            rightVal = (int) child.getValue();
        }
        value = operationType.op.apply(leftVal, rightVal);
        getParent().collapse(this);
    }

    @Override
    protected Object getValue() {
        return value;
    }

    enum OperationType {
        PLUS(Operation.Type.PLUS, (first, second) -> first + second),
        SUBTRACT(Operation.Type.SUBTRACT, (first, second) -> first - second),
        MULTIPLY(Operation.Type.MULTIPLY, (first, second) -> first * second),
        DIVIDE(Operation.Type.DIVIDE, (first, second) -> first / second),
        ;
        private final Operation.Type token;
        private final BiFunction<Integer, Integer, Integer> op;
        OperationType(Operation.Type token, BiFunction<Integer, Integer, Integer> op) {
            this.token = token;
            this.op = op;
        }

        static OperationType define(Operation.Type tokenOperation) {
            return Stream.of(values())
                    .filter(op -> op.token == tokenOperation)
                    .findFirst()
                    .orElseThrow(() -> new ParserException(ParserException.Error.UNKNOWN_OPERATION));
        }
    }
}