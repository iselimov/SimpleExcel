package com.defrag.parser;

import com.defrag.lexer.Operation;
import lombok.Getter;
import lombok.Setter;

import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.defrag.parser.ParserException.Error.DIVIDING_BY_ZERO;
import static com.defrag.parser.ParserException.Error.INTEGER_OVERFLOW;
import static com.defrag.parser.ParserException.Error.OPERATION_INVALID_ARGS;
import static com.defrag.parser.ParserException.Error.UNKNOWN_OPERATION;

/**
 * Operation node of AST
 */
class OperationExpression extends Expression {

    private final OperationType operationType;
    @Getter @Setter private Expression left;
    private int leftVal = Integer.MIN_VALUE;
    @Getter @Setter private Expression right;
    private int rightVal = Integer.MIN_VALUE;
    private int value;

    OperationExpression(Expression parent, Operation.Type tokenOpType) {
        super(Type.OPERATOR, parent);
        operationType = OperationType.define(tokenOpType);
    }

    /**
     * if we try to collapse this node while some of child
     * has literal node, we should throw an exception.
     * Otherwise we should traverse down to right child leaf
     * and collapse it also.
     *
     * @param child node to collapse
     */
    @Override
    void collapse(Expression child) {
        if (child.getType() == Type.CELL_REFERENCE) {
            Object childValue = child.getValue();
            if (childValue instanceof String) {
                throw new ParserException(OPERATION_INVALID_ARGS, this);
            }
        }
        if (child == left) {
            leftVal = (int) child.getValue();
            if (rightVal == Integer.MIN_VALUE) {
                Expression leftLeaf = leftLeaf(right);
                leftLeaf.collapse(leftLeaf);
            }
        } else if (child == right) {
            rightVal = (int) child.getValue();
        }
        try {
            value = operationType.op.apply(leftVal, rightVal);
        } catch (ParserException e) {
            throw new ParserException(e.getError(), this);
        }
        getParent().collapse(this);
    }

    @Override
    Object getValue() {
        return value;
    }

    enum OperationType {
        PLUS(Operation.Type.PLUS, (first, second) -> withRethrowEx(Math::addExact, first, second,
                () -> new ParserException(INTEGER_OVERFLOW))),
        SUBTRACT(Operation.Type.SUBTRACT, (first, second) -> withRethrowEx(Math::subtractExact, first, second,
                () -> new ParserException(INTEGER_OVERFLOW))),
        MULTIPLY(Operation.Type.MULTIPLY, (first, second) -> withRethrowEx(Math::multiplyExact, first, second,
                () -> new ParserException(INTEGER_OVERFLOW))),
        DIVIDE(Operation.Type.DIVIDE, (first, second) -> withRethrowEx((f, s) -> f / s, first, second,
                () -> new ParserException(DIVIDING_BY_ZERO)))

        ;

        private final Operation.Type token;
        private final BinaryOperator<Integer> op;
        OperationType(Operation.Type token, BinaryOperator<Integer> op) {
            this.token = token;
            this.op = op;
        }

        static OperationType define(Operation.Type tokenOperation) {
            return Stream.of(values())
                    .filter(op -> op.token == tokenOperation)
                    .findFirst()
                    .orElseThrow(() -> new ParserException(UNKNOWN_OPERATION));
        }

        private static Integer withRethrowEx(BinaryOperator<Integer> op, int first, int second, Supplier<ParserException> newEx) {
            try {
                return op.apply(first, second);
            } catch (ArithmeticException e) {
                throw newEx.get();
            }
        }
    }
}