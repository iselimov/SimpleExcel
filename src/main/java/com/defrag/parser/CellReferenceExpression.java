package com.defrag.parser;

import com.defrag.Cell;
import com.defrag.Context;
import com.defrag.lexer.Lexer;
import com.defrag.lexer.LexerException;
import com.defrag.lexer.Operation;
import com.defrag.lexer.Token;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Stack;

import static com.defrag.lexer.Token.Type.DIGIT;
import static com.defrag.lexer.Token.Type.LITERAL;
import static com.defrag.lexer.Token.Type.OPERATION;
import static com.defrag.lexer.Token.Type.REFERENCE;
import static com.defrag.parser.Expression.Type.CELL_REFERENCE;
import static com.defrag.parser.ParserException.Error.CYCLE_WAS_FOUND;
import static com.defrag.parser.ParserException.Error.OPERATION_INVALID_ARGS;
import static com.defrag.parser.ParserException.Error.TOKENS_NOT_FOUND;
import static com.defrag.parser.ParserException.Error.TOO_MANY_TOKENS;

/**
 * The cell reference node of AST, which encapsulates preparing AST for parsing
 * and also collapsing it's nodes with result setting to
 * referenced cell
 */
@Slf4j
class CellReferenceExpression extends Expression {

    @Getter private final Cell cell;
    private final Lexer lexer;
    private final Context context;
    @Getter private Expression child;
    private Object value;

    CellReferenceExpression(Cell cell, Lexer lexer, Context context) {
        this(null, cell, lexer, context);
    }

    private CellReferenceExpression(Expression parent, Cell cell, Lexer lexer, Context context) {
        super(CELL_REFERENCE, parent);
        this.cell = cell;
        this.lexer = lexer;
        this.context = context;
    }

    @Override
    void collapse(Expression child) {
        value = child.getValue();
        cell.setOutput(value);
        if (getParent() != null) {
            getParent().collapse(this);
        }
    }

    @Override
    Object getValue() {
        return value == null ? cell.getOutput() : value;
    }

    void process() {
        if (cell.isWithErrors()) {
            handleParserException(this, cell.getOutput().toString());
            return;
        }
        context.jumpToCell(cell.getIndex());
        try {
            prepare();
            collapse();
        } catch (LexerException e) {
            handleParserException(this, e.getMessage());
        } catch (ParserException e) {
            handleParserException(e.getErrNode(), e.getMessage());
        }
    }

    private void prepare() {
        if (cell.isHandled()) {
            return;
        }
        log.debug("Cell preparing started with index {}", cell.getIndex());
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
            throw new ParserException(TOKENS_NOT_FOUND, this);
        }
        cell.refreshLexerInfo();
        if (operators.isEmpty()) {
            prepareOnlyOperands(this, operands);
        } else {
            prepareOperatorsAndOperands(this, operands, operators);
        }
        log.debug("Cell preparing finished with index {}", cell.getIndex());
        cell.unmarkAsProcessing();
    }

    private void collapse() {
        if (getParent() != null || cell.isWithErrors()) {
            return;
        }
        Expression leftLeaf = leftLeaf(child);
        leftLeaf.collapse(leftLeaf);
    }

    private void handleParserException(Expression errNode, String message) {
        Expression parent = errNode;
        while (parent != null) {
            if (parent.getType() == CELL_REFERENCE) {
                CellReferenceExpression parentExpr = (CellReferenceExpression) parent;
                Cell errorCell = parentExpr.getCell();
                errorCell.setOutput(message);
                errorCell.markAsHasErrors();
                handleParserException(parentExpr.getParent(), message);
                break;
            }
            parent = parent.getParent();
        }
    }

    private void prepareOnlyOperands(Expression parent, Stack<Token> operands) {
        Token nextToken = operands.pop();
        if (!operands.isEmpty()) {
            throw new ParserException(TOO_MANY_TOKENS, this);
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
                throw new ParserException(OPERATION_INVALID_ARGS, operatorExpr);
            }
            Token leftOperand = operands.pop();
            Expression leftExpr = prepareExpression(operatorExpr, leftOperand);
            operatorExpr.setLeft(leftExpr);
            if (!operands.isEmpty() || !operators.isEmpty()) {
                throw new ParserException(OPERATION_INVALID_ARGS, operatorExpr);
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
                throw new ParserException(CYCLE_WAS_FOUND, this);
            }
            CellReferenceExpression expr = new CellReferenceExpression(parent, referenceCell, lexer, context);
            expr.process();
            result = expr;
        } else if (nextToken.getType() == DIGIT) {
            result = new DigitExpression(parent, (Integer) nextToken.getValue());
        } else if (nextToken.getType() == LITERAL) {
            result = new LiteralExpression(parent, (String) nextToken.getValue());
        } else {
            throw new ParserException(TOKENS_NOT_FOUND, this);
        }
        child = result;
        return result;
    }
}