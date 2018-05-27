package com.defrag.parser;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.Optional;

import static com.defrag.parser.Expression.Type.CELL_REFERENCE;
import static com.defrag.parser.Expression.Type.OPERATOR;

public abstract class Expression {

    @Getter private final Type type;
    @Getter(AccessLevel.PROTECTED) private Expression parent;

    enum Type {
        CELL_REFERENCE,
        CONSTANT,
        OPERATOR,
        LITERAL
    }

    protected Expression(Type type, Expression parent) {
        this.type = type;
        this.parent = parent;
    }

    protected Optional<Expression> leftLeaf(Expression next) {
        while (next != null) {
            if (next.getType() == CELL_REFERENCE) {
                next = ((CellReferenceExpression)next).getChild();
            } else if (next.getType() == OPERATOR) {
                next = ((OperationExpression)next).getLeft();
            } else {
                break;
            }
        }
        return Optional.ofNullable(next);
    }

    protected abstract void collapse(Expression child);

    protected abstract Object getValue();
}