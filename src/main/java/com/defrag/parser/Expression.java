package com.defrag.parser;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.Optional;

import static com.defrag.parser.Expression.Type.CELL_REFERENCE;
import static com.defrag.parser.Expression.Type.OPERATOR;

abstract class Expression {

    @Getter private final Type type;
    @Getter(AccessLevel.PROTECTED) private Expression parent;

    enum Type {
        CELL_REFERENCE,
        CONSTANT,
        OPERATOR,
        LITERAL
    }

    Expression(Type type, Expression parent) {
        this.type = type;
        this.parent = parent;
    }

    Expression leftLeaf(Expression root) {
        Expression next = root;
        Expression prev = next;
        while (next != null) {
            if (next.getType() == CELL_REFERENCE) {
                prev = next;
                next = ((CellReferenceExpression)next).getChild();
            } else if (next.getType() == OPERATOR) {
                prev = next;
                next = ((OperationExpression)next).getLeft();
            } else {
                break;
            }
        }
        return Optional.ofNullable(next).orElse(prev);
    }

    abstract void collapse(Expression child);

    abstract Object getValue();
}