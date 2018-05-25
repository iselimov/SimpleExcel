package com.defrag.parser;

import com.defrag.Cell;
import com.defrag.Context;
import com.defrag.lexer.Lexer;
import com.defrag.lexer.Token;

import java.util.Optional;
import java.util.Stack;

import static com.defrag.lexer.Token.Type.DIGIT;
import static com.defrag.lexer.Token.Type.LITERAL;
import static com.defrag.lexer.Token.Type.OPERATION;
import static com.defrag.lexer.Token.Type.REFERENCE;
import static com.defrag.parser.Expression.Type.CELL_REFERENCE;
import static com.defrag.parser.ParserException.Error.TOKENS_NOT_FOUND;
import static com.defrag.parser.ParserException.Error.TOO_MANY_TOKENS;

//@Slf4j
public class CellReferenceExpression extends Expression {

    private final Cell cell;
    private final Lexer lexer;
    private final Context context;
    private Expression child;

    public CellReferenceExpression(Cell cell, Lexer lexer, Context context) {
        super(CELL_REFERENCE);
        this.cell = cell;
        this.lexer = lexer;
        this.context = context;
    }

    public CellReferenceExpression(Expression parent, Cell cell, Lexer lexer, Context context) {
        super(CELL_REFERENCE, parent);
        this.cell = cell;
        this.lexer = lexer;
        this.context = context;
    }

    public void prepare() {
        cell.markAsProcessing();
        prepare(this);
    }

    private void prepare(Expression parent) {
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
            prepareOnlyOperands(parent, operands, operators);
        } else {
            prepareOperatorsAndOperands(parent, operands, operators);
        }
    }

    private void prepareOnlyOperands(Expression parent, Stack<Token> operands, Stack<Token> operators) {
        Token nextToken = operands.pop();
        if (!operators.isEmpty()) {
            throw new ParserException(TOO_MANY_TOKENS);
        }
        Expression expr = prepareExpression(parent, nextToken, cell);
        if (expr.getType() == CELL_REFERENCE) {
            prepare(expr);
        }
    }

    private void prepareOperatorsAndOperands(Expression parent, Stack<Token> operands, Stack<Token> operators) {
        while (!operators.isEmpty()) {
            OperationExpression operatorExpr = new OperationExpression(parent);
            Token operator = operators.pop();
            Expression rightExpr = prepareExpression(parent, operator, cell);
            if (rightExpr.getType() == CELL_REFERENCE) {
                prepare(rightExpr);
            }
            operatorExpr.setRight(rightExpr);
            Token leftOperand = operands.pop();
            Expression leftExpr = prepareExpression(parent, leftOperand, cell);
            if (rightExpr.getType() == CELL_REFERENCE) {
                prepare(leftExpr);
            }
        }
    }

    private Expression prepareExpression(Expression parent, Token nextToken, Cell currCell) {
        Expression result;
        if (nextToken.getType() == REFERENCE) {
            int cellIndex = (int) nextToken.getValue();
            Cell referenceCell = context.getCell(cellIndex);
            result = new CellReferenceExpression(parent, referenceCell, lexer, context);
            referenceCell.markAsProcessing();
            context.jumpToCell(cellIndex);
        } else if (nextToken.getType() == DIGIT) {
            result = new DigitExpression(parent, currCell, (Integer) nextToken.getValue());
        } else if (nextToken.getType() == LITERAL) {
            result = new LiteralExpression(parent, currCell, (String) nextToken.getValue());
        } else {
            throw new ParserException(TOKENS_NOT_FOUND);
        }
        child = result;
        return result;
    }
}