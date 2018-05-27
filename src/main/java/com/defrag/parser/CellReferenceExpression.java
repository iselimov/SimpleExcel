package com.defrag.parser;

import com.defrag.Cell;
import com.defrag.Context;
import com.defrag.lexer.Lexer;
import com.defrag.lexer.Operation;
import com.defrag.lexer.Token;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Stack;

import static com.defrag.lexer.Token.Type.*;
import static com.defrag.parser.Expression.Type.CELL_REFERENCE;
import static com.defrag.parser.ParserException.Error.*;

@Slf4j
public class CellReferenceExpression extends Expression {

    private final Cell cell;
    private final Lexer lexer;
    private final Context context;
    @Getter private Expression child;
    @Getter private Object value;

    public CellReferenceExpression(Cell cell, Lexer lexer, Context context) {
        this(null, cell, lexer, context);
    }

    private CellReferenceExpression(Expression parent, Cell cell, Lexer lexer, Context context) {
        super(CELL_REFERENCE, parent);
        this.cell = cell;
        this.lexer = lexer;
        this.context = context;
    }

    @Override
    protected void collapse(Expression child) {
        value = child.getValue();
        cell.setOutput(value.toString());
        if (child.getParent() != null) {
            child.getParent().collapse(this);
        }
    }
    public void collapse() {
        if (getParent() == null) {
            leftLeaf(child).ifPresent(next -> next.collapse(next));
        }
    }

    public void prepare() {
        if (cell.isHandled()) {
            return;
        }
        log.info("Cell preparing started with index {}", cell.getIndex());
        cell.markAsProcessing();
        Stack<Token> operands = new Stack<>();
        Stack<Token> operators = new Stack<>();
        Optional<Token> next;
        while ((next = lexer.next()).isPresent()) {
            Token nextToken = next.get();
            if (nextToken.getType() == OPERATION) {
                operators.push(nextToken);
            } else {
                operands.push(nextToken);
            }
        }
        if (operands.isEmpty()) {
            throw new ParserException(TOKENS_NOT_FOUND);
        }
        if (operators.isEmpty()) {
            prepareOnlyOperands(this, operands);
        } else {
            prepareOperatorsAndOperands(this, operands, operators);
        }
        log.info("Cell preparing finished with index {}", cell.getIndex());
        cell.unmarkAsProcessing();
    }

    private void prepareOnlyOperands(Expression parent, Stack<Token> operands) {
        Token nextToken = operands.pop();
        if (!operands.isEmpty()) {
            throw new ParserException(TOO_MANY_TOKENS);
        }
        prepareExpression(parent, nextToken);
    }

    private Expression prepareOperatorsAndOperands(Expression parent, Stack<Token> operands, Stack<Token> operators) {
        Operation operator = (Operation) operators.pop();
        OperationExpression operatorExpr = new OperationExpression(parent, (Operation.Type) operator.getValue());
        Token rightOperand = operands.pop();
        Expression rightExpr = prepareExpression(operatorExpr, rightOperand);
        operatorExpr.setRight(rightExpr);
        if (operators.isEmpty()) {
            if (operands.isEmpty()) {
                throw new ParserException(OPERATION_INVALID_ARGS);
            }
            Token leftOperand = operands.pop();
            Expression leftExpr = prepareExpression(operatorExpr, leftOperand);
            operatorExpr.setLeft(leftExpr);
            if (!operands.isEmpty() || !operators.isEmpty()) {
                throw new ParserException(OPERATION_INVALID_ARGS);
            }
        } else {
            Expression leftExpr = prepareOperatorsAndOperands(operatorExpr, operands, operators);
            operatorExpr.setLeft(leftExpr);
        }
        child = operatorExpr;
        return operatorExpr;
    }

    private Expression prepareExpression(Expression parent, Token nextToken) {
        Expression result;
        if (nextToken.getType() == REFERENCE) {
            int cellIndex = (int) nextToken.getValue();
            Cell referenceCell = context.getCell(cellIndex);
            if (referenceCell.isInProcessing()) {
                throw new ParserException(CYCLE_WAS_FOUND);
            }
            CellReferenceExpression expr = new CellReferenceExpression(parent, referenceCell, lexer, context);
            context.jumpToCell(cellIndex);
            expr.prepare();
            expr.collapse();
            result = expr;
        } else if (nextToken.getType() == DIGIT) {
            result = new DigitExpression(parent, (Integer) nextToken.getValue());
        } else if (nextToken.getType() == LITERAL) {
            result = new LiteralExpression(parent, (String) nextToken.getValue());
        } else {
            throw new ParserException(TOKENS_NOT_FOUND);
        }
        child = result;
        return result;
    }
}